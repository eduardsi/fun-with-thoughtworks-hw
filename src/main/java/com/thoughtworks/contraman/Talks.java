package com.thoughtworks.contraman;

import java.util.Collection;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

class Talks {

    private final Talk[] talks;

    public Talks(Talk... talks) {
        this.talks = talks;
    }

    public Collection<Talk> longestFirst() {
        return stream(talks).sorted((it, that) -> -it.duration().compareTo(that.duration())).collect(toList());
    }

}
