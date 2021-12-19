package com.example.springwebpaint.service;

import com.example.springwebpaint.domain.Board;
import com.example.springwebpaint.domain.User;
import com.example.springwebpaint.repos.BoardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MainService {

    @Autowired
    private BoardRepo boardRepo;


    public String prepareMainPage(Map<String, Object> model) {
        Iterable<Board> boards = boardRepo.findAll();
        model.put("boards", boards);
        return "main";
    }

    public String createNewBoard(User user, String boardName, Map<String, Object> model) {
        Board board = new Board(boardName, user);
        boardRepo.save(board);
        Iterable<Board> boards = boardRepo.findAll();
        model.put("boards", boards);
        return "main";
    }
}
