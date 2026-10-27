package com.fundoo.notes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag (name = "Page Controller")
@Controller
public class PageController {
    @GetMapping("/reset-password")
    public String resetPasswordPage() {
        return "reset-password";
    }
}
