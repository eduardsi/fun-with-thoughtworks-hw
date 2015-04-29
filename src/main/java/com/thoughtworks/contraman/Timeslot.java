package com.thoughtworks.contraman;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import static java.time.Duration.between;

class Timeslot {

    private static final LocalTime INFINITE = LocalTime.MAX;

    private final LocalTime startsNoEarlierThan;
    private final LocalTime endsNoEarlierThan;
    private final LocalTime endsNoLaterThan;

    private final EventType type;
    private final Collection<Event> events = new ArrayList<>();

    private Timeslot(EventType type, LocalTime startsNoEarlierThan, LocalTime endsNoEarlierThan, LocalTime endsNoLaterThan) {
        this.type = type;
        this.startsNoEarlierThan = startsNoEarlierThan;
        this.endsNoEarlierThan = endsNoEarlierThan;
        this.endsNoLaterThan = endsNoLaterThan;
        if (type == EventType.LUNCH || type == EventType.NETWORKING) {
            occupy();
        }
    }

    public boolean fits(Talk talk) {
        return spareCapacity().compareTo(talk.duration()) >= 0;
    }

    public Duration spareCapacity() {
        return minimalCapacity().plus(extraCapacity()).minus(occupiedCapacity());
    }

    private Duration minimalCapacity() {
        return between(startsNoEarlierThan, endsNoEarlierThan);
    }

    private Duration extraCapacity() {
        return between(endsNoEarlierThan, endsNoLaterThan);
    }

    private Duration occupiedCapacity() {
        return events.stream().map(Event::duration).reduce(Duration.ZERO, Duration::plus);
    }

    public LocalTime startsAt() {
        return startsNoEarlierThan;
    }

    public LocalTime endsAt() {
        return startsNoEarlierThan.plus(occupiedCapacity());
    }

    public void occupy(Talk talk) {
        Event event = eventFromTalkSpec(talk);
        events.add(event);
    }

    public void occupy() {
        Duration duration = minimalCapacity();
        if (unknownDuration()) {
            duration = Duration.ZERO;
        }

        Event event = new Event(type().toString(), startsNoEarlierThan.plus(occupiedCapacity()), duration);
        events.add(event);
    }

    private boolean unknownDuration() {
        return endsNoEarlierThan.equals(INFINITE) && endsNoLaterThan.equals(INFINITE);
    }

    private Event eventFromTalkSpec(Talk talk) {
        return new Event(talk.title(), startsNoEarlierThan.plus(occupiedCapacity()), talk.duration());
    }

    public Collection<Event> events() {
        return events;
    }

    public boolean hasGaps() {
        return minimalCapacity().compareTo(occupiedCapacity()) > 0;
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
