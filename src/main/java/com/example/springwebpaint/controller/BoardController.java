package com.example.springwebpaint.controller;

import com.example.springwebpaint.domain.Board;
import com.example.springwebpaint.loggers.EventLogger;
import com.example.springwebpaint.loggers.event.Event;
import com.example.springwebpaint.loggers.event.EventType;
import com.example.springwebpaint.repos.BoardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class BoardController {

    @Autowired
    private BoardRepo boardRepo;

    @Autowired
    private EventLogger eventLogger;

    @GetMapping("/board")
    public String main(Integer id,
                       Map<String, Object> model,
                       HttpServletRequest request) {
        Board board = boardRepo.findById(id);
        if(board == null) {
            eventLogger.logEvent(Event.level(EventType.WARN).that(request.getRemoteHost() + " tried to access non-exist board:" + id).now());
            throw new ResponseStatusException(NOT_FOUND, "Unable to find the board");
        }
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " successfully requested the board: " + id).now());
        model.put("board", board);
        return "board";
    }

}
