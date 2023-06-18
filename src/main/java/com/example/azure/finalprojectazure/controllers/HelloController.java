package com.example.azure.finalprojectazure.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class HelloController {

    @GetMapping("home")
    public ModelAndView home(@AuthenticationPrincipal(expression = "claims['name']") String name ) {
        String message = String.format( "Welcome back, %s !", name);
        ModelAndView modelAndView = new ModelAndView("starting");
        modelAndView.addObject("helloMessage", message);
        return modelAndView;
    }

}
