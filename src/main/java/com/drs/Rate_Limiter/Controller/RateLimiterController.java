package com.drs.Rate_Limiter.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RateLimiterController {

    @GetMapping("/hello")
    public String greet() {
        return "Hello World";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "Application Running";
    }
}