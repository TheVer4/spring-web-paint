package com.example.springwebpaint.controller;

import com.example.springwebpaint.domain.Board;
import com.example.springwebpaint.domain.User;
import com.example.springwebpaint.repos.BoardRepo;
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
    private BoardRepo boardRepo;

    @GetMapping("/main")
    public String main(Map<String, Object> model) {
        Iterable<Board> boards = boardRepo.findAll();
        model.put("boards", boards);
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam String boardName,
                      Map<String, Object> model) {
        Board board = new Board(boardName, user);
        boardRepo.save(board);
        Iterable<Board> boards = boardRepo.findAll();
        model.put("boards", boards);
        return "main";
    }

}
