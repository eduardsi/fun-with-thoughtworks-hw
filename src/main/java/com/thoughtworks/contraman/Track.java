package com.thoughtworks.contraman;

import java.time.LocalTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import static com.thoughtworks.contraman.util.PairwiseForEach.forEachPair;
import static java.util.stream.Collectors.toList;

class Track {

    private final LinkedList<Timeslot> timeslots = new LinkedList<>();

    public Track(TimeConstraints constraints, Consumer<Timeslot> timeslotOccupier) {
        forEachPair(constraints.chronologicalOrder(), (start, end) -> {

            Timeslot timeslot;
            LocalTime startsAt = startsAt(start.startsNoEarlierThan);
            if (end == null) {
                timeslot = Timeslot.withUnknownEndTime(start.type, startsAt);
            } else {
                timeslot = Timeslot.withKnownEndTime(start.type, startsAt, end.startsNoEarlierThan, end.startsNoLaterThan);
            }


            timeslots.add(timeslot);

            if (timeslot.type() == EventType.SESSION) {
                timeslotOccupier.accept(timeslot);
            }
        });
    }

    private LocalTime startsAt(LocalTime startsNoEarlierThan) {
        return timeslots.isEmpty() ? startsNoEarlierThan : timeslots.getLast().busyUntil();
    }

    public Collection<Event> events() {
        return timeslots.stream().flatMap(timeslot -> timeslot.events().stream()).collect(toList());
    }
}
