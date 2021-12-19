package com.example.springwebpaint.domain;

import javax.persistence.*;

@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String boardName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public User getOwner() {
        return owner;
    }

    public Board() {}

    public Board(String boardName, User owner) {
        this.boardName = boardName;
        this.owner = owner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}
