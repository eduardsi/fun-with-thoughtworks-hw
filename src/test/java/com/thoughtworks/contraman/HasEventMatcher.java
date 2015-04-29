package com.thoughtworks.contraman;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.time.LocalTime;

import static java.time.LocalTime.parse;

public class HasEventMatcher extends TypeSafeMatcher<Timetable> {

    private final LocalTime startsAt;
    private final String title;
    private final int track;

    private HasEventMatcher(int track, String startsAt, String title) {
        this.track = track;
        this.startsAt = parse(startsAt);
        this.title = title;
    }

    @Override
    protected boolean matchesSafely(Timetable timetable) {
        return timetable.events(track)
                .stream()
                .filter(event -> event.startsAt().equals(startsAt) && event.title().equals(title))
                .findAny()
                .isPresent();
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("Timetable with event ")
                .appendValueList("[", ",", "]", startsAt, title)
                .appendText(" on a track ")
                .appendValue(track);

    }

    @Override
    protected void describeMismatchSafely(Timetable item, Description mismatchDescription) {
        mismatchDescription
                .appendText("track ")
                .appendValue(track)
                .appendText(" doesn't have matching event.");
    }

    public static HasEventMatcher hasEventOnATrack(int track, String startsAt, String title) {
        return new HasEventMatcher(track, startsAt, title);
    }

    public static HasEventMatcher hasEvent(String startsAt, String title) {
        return hasEventOnATrack(0, startsAt, title);
    }

}
