package com.thoughtworks.contraman;

import java.time.Duration;

public class Talk {

    private final String title;
    private final Duration duration;

    public Talk(String title, long durationInMinutes) {
        this.title = title;
        this.duration = Duration.ofMinutes(durationInMinutes);
    }

    public String title() {
        return title;
    }

    public Duration duration() {
        return duration;
    }

}
