package com.sms.modules.iam.controller;

import com.sms.mapper.UserMapper;
import com.sms.modules.iam.domain.User;
import com.sms.modules.iam.dto.UserResponse;
import com.sms.modules.iam.service.UserService;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> list() {
        List<UserResponse> list = userService.listAll().stream().map(UserMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        return userService.getById(id).map(u -> ResponseEntity.ok(UserMapper.toResponse(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        User u = userService.create(user);
        return ResponseEntity.ok(UserMapper.toResponse(u));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody User req) {
        User u = userService.update(id, req);
        return ResponseEntity.ok(UserMapper.toResponse(u));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable String id, @RequestParam String status) {
        userService.changeStatus(id, status);
        return ResponseEntity.ok("Status updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
    
    public static void main(String[] args) {
        byte[] key = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        System.out.println(Base64.getEncoder().encodeToString(key));
    }
}
