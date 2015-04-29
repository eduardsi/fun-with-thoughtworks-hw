package com.thoughtworks.contraman.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.thoughtworks.contraman.util.PairwiseForEach.forEachPair;
import static java.util.Arrays.asList;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertThat;

public class PairwiseForEachTest {

    @Test
    public void iteratesOverPairsOfListElements() {
        // given
        List<Integer> listElements = asList(1, 2, 3, 4, 5);


        Map<Integer, Integer> pairs = new HashMap<>();
        forEachPair(listElements, pairs::put);

        assertThat(pairs, hasEntry(1, 2));
        assertThat(pairs, hasEntry(2, 3));
        assertThat(pairs, hasEntry(4, 5));
        assertThat(pairs, hasEntry(5, null));
    }

}