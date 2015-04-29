package com.thoughtworks.contraman;

interface TimeReserver {
    
    void reserveTime(Timeslot timeslot);

    boolean ofMatchingType(Timeslot timeslot);
}
