package com.sms.modules.payroll.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.modules.payroll.domain.PayrollTransaction;
import com.sms.modules.payroll.domain.SalaryStructure;
import com.sms.modules.payroll.dto.PayrollGenerateRequest;
import com.sms.modules.payroll.dto.SalaryStructureDTO;
import com.sms.modules.payroll.service.PayrollService;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    // Setup Salary for a Staff Member
    @PostMapping("/structure")
    public ResponseEntity<SalaryStructure> saveStructure(@RequestBody SalaryStructureDTO dto) {
        return ResponseEntity.ok(payrollService.saveSalaryStructure(dto));
    }

    // Generate Payroll for the whole school for a specific month
    @PostMapping("/generate")
    public ResponseEntity<List<PayrollTransaction>> generatePayroll(@RequestBody PayrollGenerateRequest request) {
        return ResponseEntity.ok(payrollService.generateMonthlyPayroll(request));
    }

    // View Payroll List (Ledger)
    @GetMapping("/view")
    public ResponseEntity<List<PayrollTransaction>> viewPayroll(
            @RequestParam String month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(payrollService.getPayrollByMonth(month, year));
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<Void> markAsPaid(@PathVariable String id) {
        payrollService.markAsPaid(id);
        return ResponseEntity.ok().build();
    }

    // 5. Get Single Slip Data (For Printing PDF)
    @GetMapping("/slip/{id}")
    public ResponseEntity<PayrollTransaction> getPayslipData(@PathVariable String id) {
        return ResponseEntity.ok(payrollService.getPayslip(id));
    }

    @GetMapping("/history/{staffId}")
    public ResponseEntity<List<PayrollTransaction>> getStaffHistory(@PathVariable String staffId) {
        return ResponseEntity.ok(payrollService.getStaffHistory(staffId));
    }
}