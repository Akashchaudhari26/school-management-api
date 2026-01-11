package com.sms.modules.fees.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.sms.modules.fees.dto.BulkFeeCreateRequest;
import com.sms.modules.fees.dto.BulkFeeResponse;
import com.sms.modules.fees.dto.FeeCreateRequest;
import com.sms.modules.fees.dto.FeePaymentHistoryResponse;
import com.sms.modules.fees.dto.FeePaymentRequest;
import com.sms.modules.fees.dto.FeeResponse;
import com.sms.modules.fees.service.FeeService;

@RestController
@RequestMapping("/api/fees")
@RequiredArgsConstructor
public class FeeController {

    private final FeeService feeService;

    // ================= CREATE FEE =================
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('FEE_MANAGE_ALL', 'FEE_CREATE')")
    public ResponseEntity<FeeResponse> createFee(
            @RequestBody FeeCreateRequest request) {

        FeeResponse response = feeService.createFee(request);
        return ResponseEntity.status(201).body(response);
    }

    // ================= PAY FEE =================
    @PostMapping(value = "/student/{studentId}/{academicYear}/pay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('FEE_MANAGE_ALL', 'FEE_CREATE')")
    public ResponseEntity<FeeResponse> payFeeByStudent(
            @PathVariable String studentId,
            @PathVariable String academicYear,
            @RequestBody FeePaymentRequest request) {

        FeeResponse response = feeService.payFeeByStudent(studentId, academicYear, request);

        return ResponseEntity.ok(response);
    }

    // ================= GET FEE SUMMARY =================
    @GetMapping(value = "/student/{studentId}/{academicYear}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('FEE_MANAGE_ALL', 'FEE_READ')")
    public ResponseEntity<FeeResponse> getFee(
            @PathVariable String studentId,
            @PathVariable String academicYear) {

        FeeResponse response = feeService.getFeeByStudent(studentId, academicYear);

        return ResponseEntity.ok(response);
    }

    // ================= DUES DASHBOARD =================
    @GetMapping(value = "/dues", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('FEE_MANAGE_ALL', 'FEE_READ')")
    public ResponseEntity<List<FeeResponse>> getPendingDues(
            @RequestParam String academicYear) {

        List<FeeResponse> dues = feeService.getPendingDues(academicYear);

        return ResponseEntity.ok(dues);
    }

    // ================= RECEIPT DOWNLOAD =================
    @GetMapping(value = "/receipt/{receiptNo}", produces = MediaType.APPLICATION_PDF_VALUE)
    @PreAuthorize("hasAnyAuthority('FEE_MANAGE_ALL', 'FEE_READ')")
    public ResponseEntity<byte[]> downloadReceipt(
            @PathVariable String receiptNo) {

        byte[] pdf = feeService.downloadReceipt(receiptNo);

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=" + receiptNo + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ================= PAYMENT HISTORY =================
    @GetMapping(value = "/student/{studentId}/{academicYear}/payments", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('FEE_MANAGE_ALL', 'FEE_READ')")
    public ResponseEntity<List<FeePaymentHistoryResponse>> getPaymentHistory(
            @PathVariable String studentId,
            @PathVariable String academicYear) {

        List<FeePaymentHistoryResponse> history = feeService.getPaymentHistory(studentId, academicYear);

        return ResponseEntity.ok(history);
    }

    @PostMapping("/bulk-create")
    @PreAuthorize("hasAnyAuthority('FEE_MANAGE_ALL', 'FEE_CREATE')")
    public ResponseEntity<BulkFeeResponse> createBulkFees(@RequestBody BulkFeeCreateRequest request) {

        BulkFeeResponse response = feeService.createBulkFees(request);

        return ResponseEntity.ok(response);
    }
}
