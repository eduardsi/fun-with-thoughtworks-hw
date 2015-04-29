package com.thoughtworks.contraman;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class TotalNumberOfTracksMatcher extends TypeSafeMatcher<Timetable> {

    private final int count;

    private TotalNumberOfTracksMatcher(int count) {
        this.count = count;
    }

    @Override
    protected boolean matchesSafely(Timetable timetable) {
        return timetable.trackCount() == count;
    }

    @Override
    public void describeTo(Description description) {
        description
                .appendText("Timetable with total number of tracks ")
                .appendValue(count);
    }

    @Override
    protected void describeMismatchSafely(Timetable item, Description mismatchDescription) {
        mismatchDescription
                .appendText("had ")
                .appendValue(item.trackCount())
                .appendText(" tracks");
    }

    public static TotalNumberOfTracksMatcher hasTotalNumberOfTracks(int count) {
        return new TotalNumberOfTracksMatcher(count);
    }


}