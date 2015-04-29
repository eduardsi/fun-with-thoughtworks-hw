package com.thoughtworks.contraman;

@FunctionalInterface
interface TimeReserverFinder {

    TimeReserver findFor(Timeslot timeslot);

}
