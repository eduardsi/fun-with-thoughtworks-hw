package com.thoughtworks.contraman;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.PrintWriter;

import static com.thoughtworks.contraman.EventType.*;
import static com.thoughtworks.contraman.HasEventMatcher.hasEvent;
import static com.thoughtworks.contraman.HasEventMatcher.hasEventOnATrack;
import static com.thoughtworks.contraman.TotalNumberOfTracksMatcher.hasTotalNumberOfTracks;
import static org.junit.Assert.assertThat;

public class TimetableTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void respectsMultipleTimeConstraintsOfSimilarType() {
        TimeConstraints timeConstraints = new TimeConstraints()
                .impose("09:00", SESSION)
                .impose("12:00", LUNCH)
                .impose("13:00", SESSION);

        Talks talks = new Talks(
                new Talk("A", 60),
                new Talk("B", 60),
                new Talk("C", 60),
                new Talk("D", 60));

        Timetable timetable = new Timetable(timeConstraints, talks);
        printTimetable(timetable);

        assertThat(timetable, hasTotalNumberOfTracks(1));
        assertThat(timetable, hasEvent("09:00", "A"));
        assertThat(timetable, hasEvent("10:00", "B"));
        assertThat(timetable, hasEvent("11:00", "C"));
        assertThat(timetable, hasEvent("12:00", "Lunch"));
        assertThat(timetable, hasEvent("13:00", "D"));
    }

    @Test
    public void respectsExactTimeConstraints() {
        TimeConstraints timeConstraints = new TimeConstraints()
                .impose("09:00", SESSION)
                .impose("12:00", LUNCH);

        Talks talks = new Talks(
                new Talk("A", 60),
                new Talk("B", 60),
                new Talk("C", 60));

        Timetable timetable = new Timetable(timeConstraints, talks);
        printTimetable(timetable);

        assertThat(timetable, hasTotalNumberOfTracks(1));
        assertThat(timetable, hasEvent("09:00", "A"));
        assertThat(timetable, hasEvent("10:00", "B"));
        assertThat(timetable, hasEvent("11:00", "C"));
        assertThat(timetable, hasEvent("12:00", "Lunch"));

        printTimetable(timetable);
    }

    @Test
    public void respectsFlexibleTimeConstraints() {
        TimeConstraints timeConstraints = new TimeConstraints()
                .impose("09:00", SESSION)
                .impose("12:00", "12:05", LUNCH);

        Talks talks = new Talks(
                new Talk("A", 60),
                new Talk("B", 60),
                new Talk("C", 65));

        Timetable timetable = new Timetable(timeConstraints, talks);
        printTimetable(timetable);

        assertThat(timetable, hasTotalNumberOfTracks(1));
        assertThat(timetable, hasEvent("09:00", "C"));
        assertThat(timetable, hasEvent("10:05", "A"));
        assertThat(timetable, hasEvent("11:05", "B"));
        assertThat(timetable, hasEvent("12:05", "Lunch"));
    }

    @Test
    public void accommodatesLongestTalksFirst() {
        TimeConstraints timeConstraints = new TimeConstraints()
                .impose("09:00", SESSION)
                .impose("11:00", LUNCH);

        Talks talks = new Talks(
                new Talk("A", 60),
                new Talk("B", 40),
                new Talk("C", 20));

        Timetable timetable = new Timetable(timeConstraints, talks);
        printTimetable(timetable);

        assertThat(timetable, hasTotalNumberOfTracks(1));
        assertThat(timetable, hasEvent("09:00", "A"));
        assertThat(timetable, hasEvent("10:00", "B"));
        assertThat(timetable, hasEvent("10:40", "C"));
        assertThat(timetable, hasEvent("11:00", "Lunch"));
    }

    @Test
    public void distributesTalksAmongMultipleTracksIfOneTrackIsNotEnough() {
        TimeConstraints timeConstraints = new TimeConstraints()
                .impose("09:00", SESSION)
                .impose("12:00", LUNCH)
                .impose("13:00", SESSION)
                .impose("14:00", NETWORKING);

        Talks talks = new Talks(
                new Talk("A", 60),
                new Talk("B", 60),
                new Talk("C", 60),
                new Talk("D", 60),

                new Talk("E", 60),
                new Talk("F", 60),
                new Talk("G", 60),
                new Talk("H", 60));

        Timetable timetable = new Timetable(timeConstraints, talks);
        printTimetable(timetable);

        assertThat(timetable, hasTotalNumberOfTracks(2));

        assertThat(timetable, hasEventOnATrack(0, "09:00", "A"));
        assertThat(timetable, hasEventOnATrack(0, "10:00", "B"));
        assertThat(timetable, hasEventOnATrack(0, "11:00", "C"));
        assertThat(timetable, hasEventOnATrack(0, "12:00", "Lunch"));
        assertThat(timetable, hasEventOnATrack(0, "13:00", "D"));
        assertThat(timetable, hasEventOnATrack(0, "14:00", "Networking Event"));

        assertThat(timetable, hasEventOnATrack(1, "09:00", "E"));
        assertThat(timetable, hasEventOnATrack(1, "10:00", "F"));
        assertThat(timetable, hasEventOnATrack(1, "11:00", "G"));
        assertThat(timetable, hasEventOnATrack(1, "12:00", "Lunch"));
        assertThat(timetable, hasEventOnATrack(1, "13:00", "H"));
        assertThat(timetable, hasEventOnATrack(1, "14:00", "Networking Event"));
    }

    @Test
    public void throwsExceptionIfTalkCannotBeAccommodated() {

        thrown.expect(NoneOfTheTalksFitsTimeslot.class);
        thrown.expectMessage("None of the talks [A] fits timeslot starting at 09:00");

        TimeConstraints timeConstraints = new TimeConstraints()
                .impose("09:00", SESSION)
                .impose("10:00", NETWORKING);

        Talks talks = new Talks(new Talk("A", 65));

        new Timetable(timeConstraints, talks);
    }

    private void printTimetable(Timetable timetable) {
        PrintWriter writer = new PrintWriter(System.out, true);
        new TimetablePrinter(timetable).print(writer);
    }


}
