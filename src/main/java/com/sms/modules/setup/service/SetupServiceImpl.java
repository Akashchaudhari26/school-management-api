package com.sms.modules.setup.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sms.modules.school.repository.SchoolRepository;
import com.sms.modules.setup.dto.SetupApplicationRequest;
import com.sms.modules.setup.dto.SetupStatusResponse;
import com.sms.modules.setup.event.SetupInitializationEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SetupServiceImpl implements SetupService {

    private final SchoolRepository schoolRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public SetupStatusResponse getStatus() {

        return new SetupStatusResponse(
                schoolRepository.existsBy());
    }

    @Override
    @Transactional
    public void initializeApplication(SetupApplicationRequest request) {

        if (schoolRepository.existsBy()) {
            throw new IllegalStateException(
                    "Application has already been initialized.");
        }

        eventPublisher.publishEvent(
                new SetupInitializationEvent(request));
    }
}