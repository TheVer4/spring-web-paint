package com.example.springwebpaint.loggers;

import com.example.springwebpaint.loggers.beans.Event;

public abstract class AbstractLogger implements EventLogger{

    private String name;

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
