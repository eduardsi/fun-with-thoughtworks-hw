package com.thoughtworks.contraman;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.stream;

class Timetable {

    private final List<Track> tracks = new LinkedList<>();

    private final TalkConsumer talks;
    private final TimeConstraints timeConstraints;
    private final TimeReserverFinder timeReserverFinder;

    public Timetable(TimeConstraints timeConstraints, Talks talksToFit) {
        this.timeConstraints = timeConstraints;
        this.talks = new TalkConsumer(talksToFit.longestFirst());

        this.timeReserverFinder = new CompositeTimeReserverFinder(
                new SessionTimeReserver(talks),
                new NetworkingTimeReserver(),
                new LunchTimeReserver());

        buildNewTrack();
    }

    private void buildNewTrack() {
        addTrack();

        if (talks.hasMoreToConsume()) {
            buildNewTrack();
        }
    }

    private void addTrack() {
        Track track = new Track(timeConstraints, timeReserverFinder);
        tracks.add(track);
    }

    public Collection<Event> events(int trackNo) {
        return tracks.get(trackNo).events();
    }

    public int trackCount() {
        return tracks.size();
    }

    private class CompositeTimeReserverFinder implements TimeReserverFinder {

        private final TimeReserver[] timeReservers;

        public CompositeTimeReserverFinder(TimeReserver... timeReservers) {
            this.timeReservers = timeReservers;
        }

        @Override
        public TimeReserver findFor(Timeslot timeslot) {
            Optional<TimeReserver> timeReserver = stream(timeReservers)
                    .filter(reserver -> reserver.ofMatchingType(timeslot))
                    .findAny();
            return timeReserver.orElseThrow(() ->
                    new IllegalStateException("Cannot find TimeReserver for timeslot of type " + timeslot.type()));
        }
    }

    private class NetworkingTimeReserver implements TimeReserver {
        @Override
        public void reserveTime(Timeslot timeslot) {
            timeslot.reserve("Networking Event");
        }

        @Override
        public boolean ofMatchingType(Timeslot timeslot) {
            return timeslot.type() == EventType.NETWORKING;
        }
    }

    private class LunchTimeReserver implements TimeReserver {
        @Override
        public void reserveTime(Timeslot timeslot) {
            timeslot.reserve("Lunch");
        }

        @Override
        public boolean ofMatchingType(Timeslot timeslot) {
            return timeslot.type() == EventType.LUNCH;
        }
    }

    private class SessionTimeReserver implements TimeReserver {
        private final TalkConsumer talks;

        public SessionTimeReserver(TalkConsumer talks) {
            this.talks = talks;
        }

        @Override
        public void reserveTime(Timeslot timeslot) {
            while ((timeslot.hasGaps() && talks.hasMoreToConsume()) || talks.hasTalkThatFits(timeslot)) {
                Talk talkThatFits = talks.consumeOneThatFits(timeslot);
                timeslot.reserve(talkThatFits.title(), talkThatFits.duration());
            }
        }

        @Override
        public boolean ofMatchingType(Timeslot timeslot) {
            return timeslot.type() == EventType.SESSION;
        }
    }

}
