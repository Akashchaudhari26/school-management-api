package com.sms.modules.setup.event.listner;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.sms.modules.iam.service.RoleService;
import com.sms.modules.setup.event.SetupInitializationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleInitializationListener {

    private final RoleService roleService;

    @EventListener
    public void handle(SetupInitializationEvent event) {
        roleService.initializeDefaultRoles(
                "");
    }
}