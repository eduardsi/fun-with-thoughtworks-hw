package com.thoughtworks.contraman;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import static com.thoughtworks.contraman.TimeCapacity.unknownCapacity;

class Timeslot {

    private static final LocalTime INFINITE = LocalTime.MAX;

    private final LocalTime startsNoEarlierThan;
    private final LocalTime endsNoEarlierThan;
    private final LocalTime endsNoLaterThan;

    private final TimeCapacity minimal;
    private final TimeCapacity reserved;
    private final TimeCapacity total;

    private final EventType type;
    private final Collection<Event> events = new ArrayList<>();

    private Timeslot(EventType type, LocalTime startsNoEarlierThan, LocalTime endsNoEarlierThan, LocalTime endsNoLaterThan) {

        this.minimal = new TimeCapacity(startsNoEarlierThan, endsNoEarlierThan);
        this.reserved = new TimeCapacity(endsNoEarlierThan, endsNoLaterThan);
        this.total = minimal.plus(reserved);

        this.type = type;
        this.startsNoEarlierThan = startsNoEarlierThan;
        this.endsNoEarlierThan = endsNoEarlierThan;
        this.endsNoLaterThan = endsNoLaterThan;
        
        type.reserve();
        
        if (type == EventType.LUNCH || type == EventType.NETWORKING) {
            reserveMinimalCapacityOrNothingIfEndTimeIsNotKnown();
        }
    }

    public boolean fits(Talk talk) {
        return free().largerOrEqualTo(talk.duration());
    }

    public void reserve(Talk talk) {
        reserve(talk.title(), talk.duration());
    }

    private void reserveMinimalCapacityOrNothingIfEndTimeIsNotKnown() {
        TimeCapacity occupy = isEndTimeUnknown() ? unknownCapacity() : minimal;
        reserve(type().toString(), occupy);
    }

    private void reserve(String title, TimeCapacity capacity) {
        Event event = new Event(title, busyUntil(), capacity);
        events.add(event);
    }

    private TimeCapacity free() {
        return total.minus(occupied());
    }

    private TimeCapacity occupied() {
        return events.stream().map(Event::duration).reduce(new TimeCapacity(0), TimeCapacity::plus);
    }

    private boolean isEndTimeUnknown() {
        return endsNoEarlierThan.equals(INFINITE) && endsNoLaterThan.equals(INFINITE);
    }

    public LocalTime startsAt() {
        return startsNoEarlierThan;
    }

    public LocalTime busyUntil() {
        return startsAt().plus(occupied().asDuration());
    }

    public Collection<Event> events() {
        return events;
    }

    public boolean hasGaps() {
        return occupied().lessThan(minimal);
    }

    public EventType type() {
        return type;
    }

    public static Timeslot withKnownEndTime(EventType type, LocalTime startsNoEarlierThan, LocalTime endsNoEarlierThan, LocalTime endsNoLaterThan) {
        return new Timeslot(type, startsNoEarlierThan, endsNoEarlierThan, endsNoLaterThan);
    }

    public static Timeslot withUnknownEndTime(EventType type, LocalTime startsNoEarlierThan) {
        return new Timeslot(type, startsNoEarlierThan, INFINITE, INFINITE);
    }
}
