package com.example.springwebpaint.controller;

import com.example.springwebpaint.domain.Drawing;
import com.example.springwebpaint.service.DrawingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drawing")
public class DrawingController {

    @Autowired
    private DrawingService drawingService;

    @GetMapping("/{board_id}")
    private ResponseEntity<Iterable<Drawing>> getDrawingsByBoardId(@PathVariable String board_id) {
        return drawingService.getDrawingsByBoardId(board_id);
    }

    @GetMapping("/{board_id}/{drawing_id}")
    private ResponseEntity<Drawing> getDrawingByBoardIdAndDrawingId(@PathVariable String board_id, @PathVariable String drawing_id) {
        return drawingService.getDrawingsByBoardIdAndDrawingId(board_id, drawing_id);
    }

    @PostMapping("/{board_id}")
    private ResponseEntity<Drawing> createDrawingByBoardId(@PathVariable String board_id, @RequestParam String json) throws JsonProcessingException {
        return drawingService.createDrawingByBoardId(board_id, json);
    }

    @DeleteMapping("/{board_id}/{drawing_id}")
    private ResponseEntity<String> deleteDrawingByBoardIdAndDrawingId(@PathVariable String board_id, @PathVariable String drawing_id) {
        return drawingService.deleteDrawingByBoardIdAndDrawingId(board_id, drawing_id);
    }

    @DeleteMapping("/{board_id}")
    private ResponseEntity<String> deleteAllDrawingsByBoardId(@PathVariable String board_id) {
        return drawingService.deleteAllDrawingsByBoardId(board_id);
    }

}
