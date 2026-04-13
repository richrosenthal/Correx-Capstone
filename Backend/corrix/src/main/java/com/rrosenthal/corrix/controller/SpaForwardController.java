package com.rrosenthal.corrix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping(value = {
            "/login",
            "/dashboard",
            "/capas",
            "/reports"
    })
    public String forwardFrontendRoutes() {
        return "forward:/index.html";
    }
}