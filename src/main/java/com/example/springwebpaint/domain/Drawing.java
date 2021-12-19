package com.example.springwebpaint.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Drawing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Board board;

    @CollectionTable(name = "DrawingType", joinColumns = @JoinColumn(name = "drawing_type_id"))
    @Enumerated(EnumType.STRING)
    private DrawingType drawing_type;

    private String coords;
    private String color;

    @Column(nullable = true)
    private String text;

    public Drawing() {}

    public Drawing(Board board, DrawingType drawing_type, String coords, String color, String text) {
        this.board = board;
        this.drawing_type = drawing_type;
        this.coords = coords;
        this.color = color;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public DrawingType getDrawing_type() {
        return drawing_type;
    }

    public void setDrawing_type(DrawingType type) {
        this.drawing_type = type;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String contents) {
        this.coords = contents;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
