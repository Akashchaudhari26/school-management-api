package com.sms.modules.payroll.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.modules.payroll.domain.PayrollTransaction;

public interface PayrollTransactionRepository extends JpaRepository<PayrollTransaction, String> {
    List<PayrollTransaction> findByMonthAndYear(String month, Integer year);

    Optional<PayrollTransaction> findByStaffIdAndMonthAndYear(String staffId, String month, Integer year);

    List<PayrollTransaction> findByStaffIdOrderByYearDescMonthDesc(String staffId);
}