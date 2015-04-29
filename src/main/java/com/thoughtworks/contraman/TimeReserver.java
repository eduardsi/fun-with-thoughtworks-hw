package com.thoughtworks.contraman;

public interface TimeReserver {
    
    void reserveTime(Timeslot timeslot);

    boolean ofMatchingType(Timeslot timeslot);
}
