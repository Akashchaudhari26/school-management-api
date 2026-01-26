package com.sms.modules.payroll.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sms.modules.payroll.domain.PayrollTransaction;
import com.sms.modules.payroll.domain.SalaryStructure;
import com.sms.modules.payroll.dto.PayrollGenerateRequest;
import com.sms.modules.payroll.dto.SalaryStructureDTO;
import com.sms.modules.payroll.repository.PayrollTransactionRepository;
import com.sms.modules.payroll.repository.SalaryStructureRepository;

@Service
public class PayrollServiceImpl implements PayrollService {

    @Autowired
    private SalaryStructureRepository salaryRepo;

    @Autowired
    private PayrollTransactionRepository payrollRepo;

    // @Autowired
    // private AttendanceService attendanceService; // LINK TO YOUR EXISTING MODULE

    // 1. Create or Update Salary Structure
    @Override
    public SalaryStructure saveSalaryStructure(SalaryStructureDTO dto) {
        SalaryStructure struct = salaryRepo.findByStaffId(dto.getStaffId())
                .orElse(new SalaryStructure());

        struct.setStaffId(dto.getStaffId());
        struct.setBasicSalary(dto.getBasicSalary());
        struct.setHra(dto.getHra());
        struct.setDa(dto.getDa());
        struct.setTransportAllowance(dto.getTransportAllowance());
        struct.setProvidentFund(dto.getProvidentFund());
        struct.setProfessionalTax(dto.getProfessionalTax());
        struct.setStaffName(dto.getStaffName() != null ? dto.getStaffName() : struct.getStaffName());

        // Auto-calculate totals
        double earnings = dto.getBasicSalary() + dto.getHra() + dto.getDa() + dto.getTransportAllowance();
        double deductions = dto.getProvidentFund() + dto.getProfessionalTax();

        struct.setGrossSalary(earnings);
        struct.setNetSalary(earnings - deductions);

        return salaryRepo.save(struct);
    }

    // 2. Generate Payroll for All Staff
    @Override
    public List<PayrollTransaction> generateMonthlyPayroll(PayrollGenerateRequest request) {
        List<SalaryStructure> allStructures = salaryRepo.findAll();
        List<PayrollTransaction> payrolls = new ArrayList<>();

        for (SalaryStructure struct : allStructures) {
            // Check if already generated to avoid duplicates
            if (payrollRepo.findByStaffIdAndMonthAndYear(struct.getStaffId(), request.getMonth(), request.getYear())
                    .isPresent()) {
                continue;
            }

            // --- INTEGRATION POINT: FETCH ATTENDANCE ---
            // AttendanceSummary att = attendanceService.getSummary(struct.getStaffId(),
            // request.getMonth(), request.getYear());
            // For now, I will assume full attendance for demonstration:
            int daysInMonth = Month.valueOf(request.getMonth().toUpperCase())
                    .length(java.time.Year.isLeap(request.getYear()));
            int presentDays = 22; // Mock data
            int paidLeaves = 2; // Mock data
            int payableDays = presentDays + paidLeaves;
            int absentDays = daysInMonth - payableDays;
            // -------------------------------------------

            PayrollTransaction txn = new PayrollTransaction();
            txn.setStaffId(struct.getStaffId());
            txn.setStaffName(struct.getStaffName());
            txn.setMonth(request.getMonth());
            txn.setYear(request.getYear());
            txn.setTotalDaysInMonth(daysInMonth);
            txn.setPresentDays(presentDays);
            txn.setPaidLeaves(paidLeaves);
            txn.setAbsentDays(absentDays);
            txn.setPayableDays(payableDays);

            // CALCULATION LOGIC (Pro-Rata Basis)
            double proRataFactor = (double) payableDays / daysInMonth;

            // Earned amounts based on attendance
            txn.setBasicEarned(Math.round(struct.getBasicSalary() * proRataFactor * 100.0) / 100.0);
            txn.setHraEarned(Math.round(struct.getHra() * proRataFactor * 100.0) / 100.0);

            // Total Earnings (Basic + HRA + DA + Transport) * Factor
            double totalEarned = Math.round(struct.getGrossSalary() * proRataFactor * 100.0) / 100.0;

            // Deductions (Usually Fixed, but can be pro-rated depending on policy. Here we
            // keep PF fixed)
            double totalDeductions = struct.getProvidentFund() + struct.getProfessionalTax();

            txn.setTotalEarnings(totalEarned);
            txn.setTotalDeductions(totalDeductions);
            txn.setNetPayable(totalEarned - totalDeductions);

            txn.setStatus("DRAFT");
            txn.setGeneratedDate(LocalDate.now());

            payrolls.add(payrollRepo.save(txn));
        }
        return payrolls;
    }

    @Override
    public List<PayrollTransaction> getPayrollByMonth(String month, Integer year) {
        return payrollRepo.findByMonthAndYear(month, year);
    }

    @Override
    public void markAsPaid(String transactionId) {
        PayrollTransaction txn = payrollRepo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        txn.setStatus("PAID");
        txn.setPaymentDate(LocalDate.now()); // Record the exact date of payment
        payrollRepo.save(txn);
    }

    // 4. Get Single Slip Details (For viewing/printing)
    @Override
    public PayrollTransaction getPayslip(String transactionId) {
        return payrollRepo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<PayrollTransaction> getStaffHistory(String staffId) {
        return payrollRepo.findByStaffIdOrderByYearDescMonthDesc(staffId);
    }
}