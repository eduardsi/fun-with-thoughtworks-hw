package com.thoughtworks.contraman;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

class Timeslot {

    private final LocalTime startsAt;
    private final LocalTime endsNoEarlierThan;
    private final LocalTime endsNoLaterThan;

    private final TimeCapacity minimal;
    private final TimeCapacity reserved;
    private final TimeCapacity total;

    private final EventType type;
    private final Collection<Event> events = new ArrayList<>();

    public Timeslot(EventType type, LocalTime startsAt, LocalTime endsNoEarlierThan, LocalTime endsNoLaterThan) {
        this.minimal = new TimeCapacity(startsAt, endsNoEarlierThan);
        this.reserved = new TimeCapacity(endsNoEarlierThan, endsNoLaterThan);
        this.total = minimal.plus(reserved);

        this.type = type;
        this.startsAt = startsAt;
        this.endsNoEarlierThan = endsNoEarlierThan;
        this.endsNoLaterThan = endsNoLaterThan;
    }

    public LocalTime startsAt() {
        return startsAt;
    }

    public Collection<Event> events() {
        return events;
    }

    public EventType type() {
        return type;
    }

    public boolean fits(TimeCapacity timeCapacity) {
        return free().largerOrEqualTo(timeCapacity);
    }

    public void reserve(String title) {
        TimeCapacity capacity = endsAtUnknownTime() ? TimeCapacity.ZERO : minimal;
        reserve(title, capacity);
    }

    private boolean endsAtUnknownTime() {
        return endsNoEarlierThan.equals(LocalTime.MAX) && endsNoLaterThan.equals(LocalTime.MAX);
    }

    public void reserve(String title, TimeCapacity capacity) {
        Event event = new Event(title, busyUntil(), capacity);
        events.add(event);
    }


    public boolean hasGaps() {
        return occupied().lessThan(minimal);
    }

    public LocalTime busyUntil() {
        return startsAt().plus(occupied().asDuration());
    }

    private TimeCapacity free() {
        return total.minus(occupied());
    }

    private TimeCapacity occupied() {
        return events.stream().map(Event::duration).reduce(TimeCapacity.ZERO, TimeCapacity::plus);
    }

}
