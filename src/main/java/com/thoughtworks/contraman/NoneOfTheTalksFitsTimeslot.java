package com.thoughtworks.contraman;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

class NoneOfTheTalksFitsTimeslot extends RuntimeException {

    private final Timeslot timeslot;
    private final List<String> talkNames;

    public NoneOfTheTalksFitsTimeslot(Timeslot timeslot, Collection<Talk> talks) {
        this.talkNames = talks.stream().map(Talk::title).collect(toList());
        this.timeslot = timeslot;
    }

    @Override
    public String getMessage() {
        return "None of the talks " + talkNames + " fits timeslot starting at " + timeslot.startsAt();
    }
}
