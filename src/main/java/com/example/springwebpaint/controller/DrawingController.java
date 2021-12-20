package com.example.springwebpaint.controller;

import com.example.springwebpaint.domain.Drawing;
import com.example.springwebpaint.loggers.EventLogger;
import com.example.springwebpaint.loggers.event.Event;
import com.example.springwebpaint.loggers.event.EventType;
import com.example.springwebpaint.service.DrawingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/drawing")
public class DrawingController {

    @Autowired
    private DrawingService drawingService;

    @Autowired
    private EventLogger eventLogger;

    @GetMapping("/{board_id}")
    private ResponseEntity<Iterable<Drawing>> getDrawingsByBoardId(@PathVariable String board_id, HttpServletRequest request) {
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " requested all drawings on board: " + board_id).now());
        return drawingService.getDrawingsByBoardId(board_id);
    }

    @GetMapping("/{board_id}/{drawing_id}")
    private ResponseEntity<Drawing> getDrawingByBoardIdAndDrawingId(@PathVariable String board_id, @PathVariable String drawing_id, HttpServletRequest request) {
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " requested drawing: " + drawing_id + " on board: " + board_id).now());
        return drawingService.getDrawingsByBoardIdAndDrawingId(board_id, drawing_id);
    }

    @PostMapping("/{board_id}")
    private ResponseEntity<Drawing> createDrawingByBoardId(@PathVariable String board_id, @RequestParam String json, HttpServletRequest request) throws JsonProcessingException {
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " requested creating new drawing on board: " + board_id).now());
        return drawingService.createDrawingByBoardId(board_id, json);
    }

    @DeleteMapping("/{board_id}/{drawing_id}")
    private ResponseEntity<String> deleteDrawingByBoardIdAndDrawingId(@PathVariable String board_id, @PathVariable String drawing_id, HttpServletRequest request) {
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " requested deletion of drawing: " + drawing_id + " on board: " + board_id).now());
        return drawingService.deleteDrawingByBoardIdAndDrawingId(board_id, drawing_id);
    }

    @DeleteMapping("/{board_id}")
    private ResponseEntity<String> deleteAllDrawingsByBoardId(@PathVariable String board_id, HttpServletRequest request) {
        eventLogger.logEvent(Event.level(EventType.INFO).that(request.getRemoteHost() + " requested deletion of all drawings on board: " + board_id).now());
        return drawingService.deleteAllDrawingsByBoardId(board_id);
    }

}
