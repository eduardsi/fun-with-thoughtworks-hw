package com.thoughtworks.contraman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

class TalkConsumer {

    private final Collection<Talk> talks;

    public TalkConsumer(Collection<Talk> talks) {
        this.talks = new ArrayList<>(talks);
    }

    public Talk consumeOneThatFits(Timeslot timeslot) {
        Optional<Talk> talkThatFitsTimeslot = talks
                .stream()
                .filter(timeslot::fits)
                .findFirst();


        Talk talk = talkThatFitsTimeslot.orElseThrow(() ->
                new NoneOfTheTalksFitsTimeslot(timeslot, talks));

        talks.remove(talk);

        return talk;
    }

    public boolean hasMoreTalks() {
        return talks.size() > 0;
    }

    public boolean hasTalkThatFits(Timeslot timeslot) {
        return talks
                .stream()
                .filter(timeslot::fits)
                .findFirst().isPresent();
    }
}
