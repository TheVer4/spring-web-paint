package com.example.springwebpaint.loggers.beans;

import java.text.DateFormat;
import java.util.Date;

public class Event {

    private EventType eventType = EventType.INFO;
    private String message = "!!!";
    private static int id= 0;
    private Date date;

    private Event() {}

    private Event(EventType type) {
        this.eventType = type;
        id++;
    }

    public static Event level(EventType type) {
        return new Event(type);
    }

    public Event that(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "<" + eventType + "> [ id=" + id + ", date=" + (DateFormat.getDateTimeInstance()).format(date) + ", msg=" + message + " ]";
    }

    public Event at(Date date) {
        this.date = date;
        return this;
    }

    public Event now() {
        this.date = new Date();
        return this;
    }
}
