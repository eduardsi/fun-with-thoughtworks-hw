package com.thoughtworks.contraman;

public class Talk {

    private final String title;
    private final TimeCapacity duration;

    public Talk(String title, long durationInMinutes) {
        this.title = title;
        this.duration = new TimeCapacity(durationInMinutes);
    }

    public String title() {
        return title;
    }

    public TimeCapacity duration() {
        return duration;
    }

}
