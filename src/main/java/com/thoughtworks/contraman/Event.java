package com.thoughtworks.contraman;

import java.time.Duration;
import java.time.LocalTime;

class Event {

    private final String title;
    private final LocalTime startsAt;
    private final Duration duration;

    public Event(String title, LocalTime startsAt, Duration duration) {
        this.title = title;
        this.startsAt = startsAt;
        this.duration = duration;
    }

    public LocalTime startsAt() {
        return startsAt;
    }

    public String title() {
        return title;
    }

    public Duration duration() {
        return duration;
    }

}
