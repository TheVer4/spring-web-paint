package com.example.springwebpaint.controller;

import com.example.springwebpaint.domain.Board;
import com.example.springwebpaint.repos.BoardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class BoardController {

    @Autowired
    private BoardRepo boardRepo;

    @GetMapping("/board")
    public String main(Integer id,
                       Map<String, Object> model) {
        Board board = boardRepo.findById(id);
        if(board == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find the board");
        }
        model.put("board", board);
        return "board";
    }

}
