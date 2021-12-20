package com.example.springwebpaint.controller;

import com.example.springwebpaint.loggers.EventLogger;
import com.example.springwebpaint.loggers.beans.Event;
import com.example.springwebpaint.loggers.beans.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private EventLogger eventLogger;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " called index page").now());
        return "index";
    }

}
