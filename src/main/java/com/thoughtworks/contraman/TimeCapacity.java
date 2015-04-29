package com.thoughtworks.contraman;

import java.time.Duration;
import java.time.LocalTime;

import static java.time.Duration.between;

public class TimeCapacity implements Comparable<TimeCapacity> {

    private final Duration duration;

    public TimeCapacity(LocalTime startsNoEarlierThan, LocalTime endsNoEarlierThan) {
        duration = between(startsNoEarlierThan, endsNoEarlierThan);
    }

    public TimeCapacity(long inMinutes) {
        this.duration = Duration.ofMinutes(inMinutes);
    }

    public TimeCapacity plus(TimeCapacity other) {
        return new TimeCapacity(this.duration.plus(other.duration).toMinutes());
    }

    public TimeCapacity minus(TimeCapacity other) {
        return new TimeCapacity(this.duration.minus(other.duration).toMinutes());
    }

    public boolean largerOrEqualTo(TimeCapacity other) {
        return duration.compareTo(other.duration) >= 0;
    }

    public boolean lessThan(TimeCapacity other) {
        return duration.compareTo(other.duration) < 0;
    }

    @Override
    public int compareTo(TimeCapacity other) {
        return -duration.compareTo(other.duration);
    }

    public static TimeCapacity unknownCapacity() {
        return new TimeCapacity(0);
    }

    public Duration asDuration() {
        return duration;
    }

    public boolean isUnknown() {
        return this.equals(unknownCapacity());
    }

    @Override
    public String toString() {
        return duration.toMinutes() + "min";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TimeCapacity && ((TimeCapacity) obj).duration.equals(duration);
    }

    @Override
    public int hashCode() {
        return duration.hashCode();
    }
}
