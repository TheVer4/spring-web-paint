package com.example.springwebpaint.controller;

import com.example.springwebpaint.domain.Board;
import com.example.springwebpaint.domain.Drawing;
import com.example.springwebpaint.domain.DrawingType;
import com.example.springwebpaint.repos.BoardRepo;
import com.example.springwebpaint.repos.DrawingRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/drawing")
public class DrawingController {

    @Autowired
    private DrawingRepo drawingRepo;

    @Autowired
    private BoardRepo boardRepo;

    @GetMapping("/{board_id}")
    private ResponseEntity<Iterable<Drawing>> getDrawingsByBoardId(@PathVariable String board_id) {
        Iterable<Drawing> drawings = drawingRepo.findByBoard_id(Integer.valueOf(board_id));
        /*if(drawings == null || !drawings.iterator().hasNext())
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);*/

        return new ResponseEntity<>(drawings, HttpStatus.OK);
    }

    @GetMapping("/{board_id}/{drawing_id}")
    private ResponseEntity<Drawing> getDrawingByBoardIdAndDrawingId(@PathVariable String board_id, @PathVariable String drawing_id) {
        Drawing drawing = drawingRepo.findByBoard_idAndId(Integer.valueOf(board_id), Long.valueOf(drawing_id));
        if(drawing == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(drawing, HttpStatus.OK);
    }

    @PostMapping("/{board_id}")
    private ResponseEntity<Drawing> createDrawingByBoardId(@PathVariable String board_id, @RequestParam String json) throws JsonProcessingException {
        Board board = boardRepo.findById(Integer.valueOf(board_id));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        String text = null;
        try {
            text = jsonNode.get("text").asText();
        } catch(Exception ignored) {}
        Drawing drawing = new Drawing(board,
                DrawingType.valueOf(jsonNode.get("drawing_type").asText()),
                mapper.writeValueAsString(jsonNode.get("coords")),
                jsonNode.get("color").asText(),
                text);
        drawingRepo.save(drawing);
        return new ResponseEntity<>(drawing, HttpStatus.OK);
    }

    @DeleteMapping("/{board_id}/{drawing_id}")
    private ResponseEntity<String> deleteDrawingByBoardIdAndDrawingId(@PathVariable String board_id, @PathVariable String drawing_id) {
        Drawing drawing = drawingRepo.findByBoard_idAndId(Integer.valueOf(board_id), Long.valueOf(drawing_id));
        if(drawing == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        drawingRepo.delete(drawing);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @DeleteMapping("/{board_id}")
    private ResponseEntity<String> deleteDrawingByBoardId(@PathVariable String board_id) {
        Iterable<Drawing> drawings = drawingRepo.findByBoard_id(Integer.valueOf(board_id));

        drawingRepo.deleteAll(drawings);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}
