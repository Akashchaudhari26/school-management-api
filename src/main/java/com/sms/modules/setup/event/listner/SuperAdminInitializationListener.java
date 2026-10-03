package com.sms.modules.setup.event.listner;

import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sms.modules.iam.domain.RoleName;
import com.sms.modules.iam.domain.User;
import com.sms.modules.iam.service.PermissionRegistry;
import com.sms.modules.iam.service.UserService;
import com.sms.modules.setup.event.SetupInitializationEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SuperAdminInitializationListener {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void handle(SetupInitializationEvent event) {

        String email = event.request().admin().email().trim().toLowerCase();
        var existingAdmin = userService.getByEmail(email);
        if (existingAdmin.isPresent()) {
            if (RoleName.SUPER_ADMIN.equals(existingAdmin.get().getRoleName())) {
                return;
            }
            throw new IllegalStateException("The setup email is already assigned to another role.");
        }

        User admin = new User();

        admin.setFullName(event.request().admin().fullName());
        admin.setEmail(email);
        admin.setMobile(event.request().admin().mobile());
        admin.setAdharNumber("000000000000");
        admin.setPassword(passwordEncoder.encode(event.request().admin().password()));

        admin.setRoleName(RoleName.SUPER_ADMIN);
        admin.setPermissions(PermissionRegistry.all());
        admin.setStatus("ACTIVE");

        userService.create(admin);
    }
}
