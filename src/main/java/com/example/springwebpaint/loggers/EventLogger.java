package com.example.springwebpaint.loggers;

import com.example.springwebpaint.loggers.event.Event;

public interface EventLogger {

    void logEvent(Event event);

    String getName();

}
