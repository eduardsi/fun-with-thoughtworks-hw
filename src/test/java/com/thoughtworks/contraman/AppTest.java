package com.thoughtworks.contraman;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static com.thoughtworks.contraman.HasEventMatcher.hasEventOnATrack;
import static com.thoughtworks.contraman.TotalNumberOfTracksMatcher.hasTotalNumberOfTracks;
import static java.nio.file.Paths.get;
import static org.junit.Assert.assertThat;

public class AppTest {

    private App app;

    @Before
    public void readTalksFromFile() throws URISyntaxException, IOException {
        Path testFile = get(getClass().getResource("/AppTest.data").toURI());
        app = new App(testFile);
    }

    @Test
    public void buildsScheduleAccordingToRulesDefinedInAcceptanceCriteria() throws IOException {

        Timetable timetable = app.buildAndPrintTimetable();

        assertThat(timetable, hasTotalNumberOfTracks(2));
        assertThat(timetable, hasEventOnATrack(0, "09:00", "Writing Fast Tests Against Enterprise Rails"));
        assertThat(timetable, hasEventOnATrack(0, "10:00", "Communicating Over Distance"));
        assertThat(timetable, hasEventOnATrack(0, "11:00", "Rails Magic"));
        assertThat(timetable, hasEventOnATrack(0, "12:00", "Lunch"));
        assertThat(timetable, hasEventOnATrack(0, "13:00", "Ruby on Rails: Why We Should Move On"));
        assertThat(timetable, hasEventOnATrack(0, "14:00", "Ruby on Rails Legacy App Maintenance"));
        assertThat(timetable, hasEventOnATrack(0, "15:00", "Overdoing it in Python"));
        assertThat(timetable, hasEventOnATrack(0, "15:45", "Ruby Errors from Mismatched Gem Versions"));
        assertThat(timetable, hasEventOnATrack(0, "16:30", "Lua for the Masses"));
        assertThat(timetable, hasEventOnATrack(0, "17:00", "Networking Event"));

        assertThat(timetable, hasEventOnATrack(1, "09:00", "Common Ruby Errors"));
        assertThat(timetable, hasEventOnATrack(1, "09:45", "Accounting-Driven Development"));
        assertThat(timetable, hasEventOnATrack(1, "10:30", "Pair Programming vs Noise"));
        assertThat(timetable, hasEventOnATrack(1, "11:15", "Clojure Ate Scala (on my project)"));
        assertThat(timetable, hasEventOnATrack(1, "12:00", "Lunch"));
        assertThat(timetable, hasEventOnATrack(1, "13:00", "Woah"));
        assertThat(timetable, hasEventOnATrack(1, "13:30", "Sit Down and Write"));
        assertThat(timetable, hasEventOnATrack(1, "14:00", "Programming in the Boondocks of Seattle"));
        assertThat(timetable, hasEventOnATrack(1, "14:30", "Ruby vs. Clojure for Back-End Development"));
        assertThat(timetable, hasEventOnATrack(1, "15:00", "A World Without HackerNews"));
        assertThat(timetable, hasEventOnATrack(1, "15:30", "User Interface CSS in Rails Apps"));
        assertThat(timetable, hasEventOnATrack(1, "16:00", "Rails for Python Developers"));
        assertThat(timetable, hasEventOnATrack(1, "16:05", "Networking Event"));

    }

}
