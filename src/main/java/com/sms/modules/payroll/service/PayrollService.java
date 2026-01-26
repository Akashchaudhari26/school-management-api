package com.sms.modules.payroll.service;

import java.util.List;

import com.sms.modules.payroll.domain.PayrollTransaction;
import com.sms.modules.payroll.domain.SalaryStructure;
import com.sms.modules.payroll.dto.PayrollGenerateRequest;
import com.sms.modules.payroll.dto.SalaryStructureDTO;

public interface PayrollService {

    SalaryStructure saveSalaryStructure(SalaryStructureDTO dto);

    List<PayrollTransaction> generateMonthlyPayroll(PayrollGenerateRequest request);

    List<PayrollTransaction> getPayrollByMonth(String month, Integer year);

    public void markAsPaid(String transactionId);

    public PayrollTransaction getPayslip(String transactionId);

    List<PayrollTransaction> getStaffHistory(String staffId);

}
