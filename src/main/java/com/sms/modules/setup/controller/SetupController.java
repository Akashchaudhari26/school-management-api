package com.sms.modules.setup.controller;

import com.sms.modules.setup.dto.SetupApplicationRequest;
import com.sms.modules.setup.dto.SetupStatusResponse;
import com.sms.modules.setup.service.SetupService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setup")
@RequiredArgsConstructor
public class SetupController {

    private final SetupService setupService;

    @GetMapping("/status")
    public ResponseEntity<SetupStatusResponse> getSetupStatus() {

        return ResponseEntity.ok(
                setupService.getStatus());
    }

    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeApplication(
            @Valid @RequestBody SetupApplicationRequest request) {

        setupService.initializeApplication(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}