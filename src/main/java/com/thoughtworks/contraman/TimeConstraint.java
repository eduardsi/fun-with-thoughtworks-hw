package com.thoughtworks.contraman;

import java.time.LocalTime;

class TimeConstraint {

    final LocalTime startsNoEarlierThan;
    final LocalTime startsNoLaterThan;
    final EventType type;

    TimeConstraint(LocalTime startsNoEarlierThan, LocalTime startsNoLaterThan, EventType type) {
        this.startsNoEarlierThan = startsNoEarlierThan;
        this.startsNoLaterThan = startsNoLaterThan;
        this.type = type;
    }
}