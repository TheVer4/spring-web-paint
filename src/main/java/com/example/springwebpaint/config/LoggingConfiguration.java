package com.example.springwebpaint.config;

import com.example.springwebpaint.loggers.CachedFileEventLogger;
import com.example.springwebpaint.loggers.EventLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {

    @Bean
    public EventLogger getEventLogger() {
        return new CachedFileEventLogger("target/events_log.txt", 5);
    }

}
