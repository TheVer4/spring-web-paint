package com.example.springwebpaint.controller;

import com.example.springwebpaint.domain.User;
import com.example.springwebpaint.loggers.EventLogger;
import com.example.springwebpaint.loggers.event.Event;
import com.example.springwebpaint.loggers.event.EventType;
import com.example.springwebpaint.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @Autowired
    private EventLogger eventLogger;

    @GetMapping("/main")
    public String main(Map<String, Object> model, HttpServletRequest request) {
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " entered the main page").now());
        return mainService.prepareMainPage(model);
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String boardName,
                      Map<String, Object> model,
                      HttpServletRequest request) {
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " created new board: " + boardName).now());
        return mainService.createNewBoard(user, boardName, model);
    }

}
