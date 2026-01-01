package com.sms.modules.iam.controller;

import com.sms.modules.iam.domain.Role;
import com.sms.modules.iam.dto.CreateRoleRequest;
import com.sms.modules.iam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateRoleRequest req) {
        Role r = new Role();
        r.setName(req.getName());
        r.setDescription(req.getDescription());
        r.setPermissions(req.getPermissions());
        r.setDefault(req.isDefaultRole());
        r.setTenantId(req.getTenantId());
        Role saved = roleService.create(r);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_READ')")
    public ResponseEntity<?> list() {
        List<Role> roles = roleService.list();
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Role req) {
        Role updated = roleService.update(id, req);
        return ResponseEntity.ok(updated);
    }
}
