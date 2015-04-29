package com.thoughtworks.contraman;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.thoughtworks.contraman.EventType.*;

class App {

    private final Path inputFile;

    public App(Path inputFile) {
        this.inputFile = inputFile;
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("The first argument must be a string pointing to input file");
        }

        Path inputFile = Paths.get(args[0]);

        new App(inputFile).buildAndPrintTimetable();
    }

    Timetable buildAndPrintTimetable() throws IOException {
        Timetable timetable = new Timetable(timeConstraints(), talks());
        TimetablePrinter printer = new TimetablePrinter(timetable);
        printer.print(sysOut());

        return timetable;
    }

    private TimeConstraints timeConstraints() {
        return new TimeConstraints()
                .impose("09:00", SESSION)
                .impose("12:00", LUNCH)
                .impose("13:00", SESSION)
                .impose("16:00", "17:00", NETWORKING);
    }

    private Talks talks() throws IOException {
        return new TalksFileReader().read(inputFile);
    }

    private static PrintWriter sysOut() {
        return new PrintWriter(System.out, true);
    }

}
