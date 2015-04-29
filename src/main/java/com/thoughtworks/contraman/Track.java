package com.thoughtworks.contraman;

import java.time.LocalTime;
import java.util.Collection;
import java.util.LinkedList;

import static com.thoughtworks.contraman.util.PairwiseForEach.forEachPair;
import static java.util.stream.Collectors.toList;

class Track {

    private final LinkedList<Timeslot> timeslots = new LinkedList<>();

    public Track(TimeConstraints constraints, TimeReserverFinder timeReserverFinder) {
        forEachPair(constraints.chronologicalOrder(), (start, end) -> {

            LocalTime startsAt = startsAt(start.startsNoEarlierThan);

            LocalTime endsNoEarlierThan = end == null ? LocalTime.MAX : end.startsNoEarlierThan;
            LocalTime endsNoLaterThan = end == null ? LocalTime.MAX : end.startsNoLaterThan;

            Timeslot timeslot = new Timeslot(start.type, startsAt, endsNoEarlierThan, endsNoLaterThan);
            timeslots.add(timeslot);

            TimeReserver timeReserver = timeReserverFinder.findFor(timeslot);
            timeReserver.reserveTime(timeslot);
        });
    }

    private LocalTime startsAt(LocalTime startsNoEarlierThan) {
        return timeslots.isEmpty() ? startsNoEarlierThan : timeslots.getLast().busyUntil();
    }

    public Collection<Event> events() {
        return timeslots.stream().flatMap(timeslot -> timeslot.events().stream()).collect(toList());
    }
}
