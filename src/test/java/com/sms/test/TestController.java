package com.sms.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/secure")
    public String securedApi() {
        return "You accessed a PROTECTED endpoint!";
    }
}