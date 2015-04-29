package com.thoughtworks.contraman;

import org.junit.Test;

import java.util.List;

import static com.thoughtworks.contraman.EventType.SESSION;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class TimeConstraintsTest {

    @Test
    public void returnsConstraintsInChronologicalOrder() {
        TimeConstraints timeConstraints = new TimeConstraints()
                .impose("12:00", SESSION)
                .impose("11:00", SESSION)
                .impose("13:00", SESSION);

        List<Integer> expectedOrder = timeConstraints.chronologicalOrder()
                .stream()
                .map(t -> t.startsNoEarlierThan.getHour())
                .collect(toList());

        assertThat(expectedOrder, contains(11, 12, 13));
    }

}