package com.thoughtworks.contraman;

import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class TimetablePrinter {

    private final Timetable timetable;

    public TimetablePrinter(Timetable timetable) {
        this.timetable = timetable;
    }

    public void print(PrintWriter writer) {
        for (int track = 0; track < timetable.trackCount(); track++) {
            printTrack(writer, track);
        }
    }

    private void printTrack(PrintWriter writer, int track) {
        writer.println("Track " + (track + 1) + ":");
        timetable.events(track).forEach(event -> printEvent(writer, event));
    }

    private void printEvent(PrintWriter writer, Event event) {
        String durationAppendix = durationAppendix(event);
        writer.println(formatTime(event.startsAt()) + " " + event.title() + durationAppendix);
    }

    private String durationAppendix(Event event) {
        TimeCapacity duration = event.duration();
        if (duration.isUnknown()) {
            return "";
        }
        return " " + duration;
    }

    private String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mma"));
    }
}
