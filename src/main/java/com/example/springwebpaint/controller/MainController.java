package com.example.springwebpaint.controller;

import com.example.springwebpaint.domain.User;
import com.example.springwebpaint.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        return mainService.prepareMainPage(model);
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String boardName,
                      Map<String, Object> model) {
        return mainService.createNewBoard(user, boardName, model);
    }

}
