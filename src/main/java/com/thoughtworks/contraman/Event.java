package com.thoughtworks.contraman;

import java.time.LocalTime;

class Event {

    private final String title;
    private final LocalTime startsAt;
    private final TimeCapacity duration;

    public Event(String title, LocalTime startsAt, TimeCapacity duration) {
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

    public TimeCapacity duration() {
        return duration;
    }

}
