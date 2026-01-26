package com.sms.modules.payroll.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sms.modules.payroll.domain.PayrollTransaction;

public interface PayrollTransactionRepository extends MongoRepository<PayrollTransaction, String> {
    List<PayrollTransaction> findByMonthAndYear(String month, Integer year);

    Optional<PayrollTransaction> findByStaffIdAndMonthAndYear(String staffId, String month, Integer year);

    List<PayrollTransaction> findByStaffIdOrderByYearDescMonthDesc(String staffId);
}