package com.example.springwebpaint.loggers;

import com.example.springwebpaint.loggers.beans.Event;

public interface EventLogger {

    void logEvent(Event event);

    String getName();

}
