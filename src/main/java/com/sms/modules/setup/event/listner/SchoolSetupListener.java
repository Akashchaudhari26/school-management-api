package com.sms.modules.setup.event.listner;

import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

import com.sms.modules.school.dto.SchoolResponse;
import com.sms.modules.school.service.SchoolService;
import com.sms.modules.setup.event.SetupInitializationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SchoolSetupListener {

    private final SchoolService schoolService;

    @EventListener
    public void handle(SetupInitializationEvent event) {

        SchoolResponse school = schoolService.createSchool(
                event.request().school());
    }
}
