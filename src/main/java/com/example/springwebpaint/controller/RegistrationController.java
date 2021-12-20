package com.example.springwebpaint.controller;

import com.example.springwebpaint.domain.User;
import com.example.springwebpaint.loggers.EventLogger;
import com.example.springwebpaint.loggers.beans.Event;
import com.example.springwebpaint.loggers.beans.EventType;
import com.example.springwebpaint.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EventLogger eventLogger;

    @GetMapping("/registration")
    public String registration(HttpServletRequest request) {
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " entered the registration page").now());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(User user, Map<String, Object> model, HttpServletRequest request) {
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " made request to register new user").now());
        return registrationService.registerUser(user, model);
    }

}
