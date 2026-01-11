package com.sms.modules.fees.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sms.modules.fees.domain.ClassFeeMaster;
import com.sms.modules.fees.domain.FeeItem;
import com.sms.modules.fees.repository.ClassFeeMasterRepository;

@RestController
@RequestMapping("/api/fees-masters")
public class FeeMasterController {

    @Autowired
    private ClassFeeMasterRepository repository;

    @PostMapping
    public ResponseEntity<ClassFeeMaster> defineClassFee(@RequestBody ClassFeeMaster request) {
        // Calculate Total automatically before saving
        BigDecimal total = request.getFeeItems().stream()
                .map(FeeItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        request.setTotalAmount(total);

        return ResponseEntity.ok(repository.save(request));
    }

    @GetMapping("/{academicYear}")
    public ResponseEntity<List<ClassFeeMaster>> getAll(@PathVariable String academicYear) {
        return ResponseEntity.ok(repository.findByAcademicYear(academicYear));
    }
}