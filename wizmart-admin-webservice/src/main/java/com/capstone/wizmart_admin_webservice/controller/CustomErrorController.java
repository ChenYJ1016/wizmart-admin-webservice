package com.capstone.wizmart_admin_webservice.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model) {
        // You might set some attributes to the model if needed
        return "customError"; // Render customError.ftl
    }

    public String getErrorPath() {
        return "/error";
    }
}
