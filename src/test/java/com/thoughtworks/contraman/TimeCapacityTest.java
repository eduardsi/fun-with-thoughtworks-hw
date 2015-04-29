package com.thoughtworks.contraman;

import org.junit.Test;

import java.time.Duration;
import java.util.List;

import static java.time.LocalTime.parse;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TimeCapacityTest {

    @Test
    public void supportsConstructionFromMinutesAndRange() {
        assertThat(capacity(30), is(capacity("09:00", "09:30")));
    }

    @Test
    public void isKnownIfNotZero() {
        assertThat(capacity(1).isUnknown(), is(false));
    }

    @Test
    public void supportsConstructionWithUnknownCapacity() {
        TimeCapacity unknownCapacity = TimeCapacity.unknownCapacity();

        assertThat(unknownCapacity, is(capacity(0)));
        assertThat(unknownCapacity.isUnknown(), is(true));
        assertThat(unknownCapacity.isUnknown(), is(true));
    }

    @Test
    public void producesToStringWithUnits() {
        assertThat(capacity(0).toString(), is("0min"));
        assertThat(capacity(1).toString(), is("1min"));
    }
    
    @Test
    public void exposesDuration() {
        assertThat(capacity(1).asDuration(), is(Duration.ofMinutes(1)));
    }

    @Test
    public void supportsConcatenation() {
        assertThat(capacity(30).plus(capacity(30)), is(capacity(60)));
    }

    @Test
    public void supportsSubtraction() {
        assertThat(capacity(60).minus(capacity(30)), is(capacity(30)));
    }

    @Test
    public void supportsLargerOrEqualsToComparison() {
        assertThat(capacity(30).largerOrEqualTo(capacity(30)), is(true));
        assertThat(capacity(31).largerOrEqualTo(capacity(30)), is(true));
        assertThat(capacity(30).largerOrEqualTo(capacity(31)), is(false));
    }

    @Test
    public void supportsLessThanComparison() {
        assertThat(capacity(30).lessThan(capacity(31)), is(true));
        assertThat(capacity(30).lessThan(capacity(30)), is(false));
        assertThat(capacity(31).lessThan(capacity(30)), is(false));
    }

    @Test
    public void implementsComparatorWhichReturnsLargestItemsFirst() {
        List<TimeCapacity> actual = asList(capacity(30), capacity(31), capacity(29))
                .stream()
                .sorted()
                .collect(toList());

        assertThat(actual, contains(capacity(31), capacity(30), capacity(29)));
    }


    private static TimeCapacity capacity(long inMinutes) {
        return new TimeCapacity(inMinutes);
    }

    private static TimeCapacity capacity(String from, String to) {
        return new TimeCapacity(parse(from), parse(to));
    }


}