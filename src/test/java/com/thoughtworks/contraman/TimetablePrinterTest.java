package com.thoughtworks.contraman;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.PrintWriter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class TimetablePrinterTest {

    @Captor
    ArgumentCaptor<String> printlnCaptor;

    @Mock
    PrintWriter writer;

    int lineCounter;

    @Before
    public void setUp() {
        lineCounter = 0;
    }

    @Test
    public void prettyPrintsTimetable() {
        // given
        TimeConstraints timeConstraints = new TimeConstraints()
                .impose("09:00", EventType.SESSION)
                .impose("11:00", EventType.LUNCH)
                .impose("13:00", EventType.SESSION)
                .impose("14:00", EventType.NETWORKING);

        Talks talks = new Talks(
                new Talk("Writing Fast Tests Against Enterprise Rails", 60),
                new Talk("Overdoing it in Python", 60),
                new Talk("Common Ruby Errors", 60),
                new Talk("Pair Programming vs Noise", 60),
                new Talk("Lean Startup", 60),
                new Talk("Continuous Delivery", 30),
                new Talk("Refactoring To Patterns", 30));

        Timetable timetable = new Timetable(timeConstraints, talks);
        TimetablePrinter timetablePrinter = new TimetablePrinter(timetable);

        // when
        timetablePrinter.print(writer);

        // then
        verify(writer, times(13)).println(printlnCaptor.capture());

        // and
        assertLine("Track 1:");
        assertLine("09:00AM Writing Fast Tests Against Enterprise Rails 60min");
        assertLine("10:00AM Overdoing it in Python 60min");
        assertLine("11:00AM Lunch 120min");
        assertLine("13:00PM Common Ruby Errors 60min");
        assertLine("14:00PM Networking Event");
        assertLine("Track 2:");
        assertLine("09:00AM Pair Programming vs Noise 60min");
        assertLine("10:00AM Lean Startup 60min");
        assertLine("11:00AM Lunch 120min");
        assertLine("13:00PM Continuous Delivery 30min");
        assertLine("13:30PM Refactoring To Patterns 30min");
        assertLine("14:00PM Networking Event");
    }

    private void assertLine(String text) {
        assertThat(printlnCaptor.getAllValues().get(lineCounter++), is(text));
    }

}
