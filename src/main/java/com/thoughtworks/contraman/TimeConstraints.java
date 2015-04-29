package com.thoughtworks.contraman;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalTime.parse;
import static java.util.stream.Collectors.toList;

public class TimeConstraints {

    private final List<TimeConstraint> constraints = new ArrayList<>();

    public TimeConstraints impose(String startsAt, EventType type) {
        return impose(startsAt, startsAt, type);
    }

    public TimeConstraints impose(String startsNoEarlierThan, String startsNoLaterThan, EventType type) {
        TimeConstraint constraint = new TimeConstraint(parse(startsNoEarlierThan), parse(startsNoLaterThan), type);
        constraints.add(constraint);
        return this;
    }

    public List<TimeConstraint> chronologicalOrder() {
        return constraints
                .stream()
                .sorted((it, that) -> it.startsNoEarlierThan.compareTo(that.startsNoLaterThan))
                .collect(toList());
    }

}
