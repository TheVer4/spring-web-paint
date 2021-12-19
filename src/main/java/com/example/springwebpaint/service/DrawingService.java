package com.example.springwebpaint.service;

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
import org.springframework.stereotype.Service;

@Service
public class DrawingService {

    @Autowired
    private DrawingRepo drawingRepo;

    @Autowired
    private BoardRepo boardRepo;


    public ResponseEntity<Iterable<Drawing>> getDrawingsByBoardId(String board_id) {
        Iterable<Drawing> drawings = drawingRepo.findByBoard_id(Integer.valueOf(board_id));
        return new ResponseEntity<>(drawings, HttpStatus.OK);
    }

    public ResponseEntity<Drawing> getDrawingsByBoardIdAndDrawingId(String board_id, String drawing_id) {
        Drawing drawing = drawingRepo.findByBoard_idAndId(Integer.valueOf(board_id), Long.valueOf(drawing_id));
        if(drawing == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(drawing, HttpStatus.OK);
    }

    public ResponseEntity<Drawing> createDrawingByBoardId(String board_id, String json) throws JsonProcessingException {
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

    public ResponseEntity<String> deleteDrawingByBoardIdAndDrawingId(String board_id, String drawing_id) {
        Drawing drawing = drawingRepo.findByBoard_idAndId(Integer.valueOf(board_id), Long.valueOf(drawing_id));
        if(drawing == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        drawingRepo.delete(drawing);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    public ResponseEntity<String> deleteAllDrawingsByBoardId(String board_id) {
        Iterable<Drawing> drawings = drawingRepo.findByBoard_id(Integer.valueOf(board_id));
        drawingRepo.deleteAll(drawings);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
