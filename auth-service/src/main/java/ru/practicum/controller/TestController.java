package ru.practicum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/admin")
    public String createSmth() {
        return "Success! Only for admin";
    }

    @GetMapping("/user")
    public String getSmth() {
        return "Success! Only for users!";
    }
}
