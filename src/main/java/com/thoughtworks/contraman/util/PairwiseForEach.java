package com.thoughtworks.contraman.util;

import java.util.List;
import java.util.function.BiConsumer;

public class PairwiseForEach {

    private PairwiseForEach() {
    }

    public static <T> void forEachPair(List<T> collection, BiConsumer<T, T> consumer) {
        int collectionSize = collection.size();

        for (int current = 0, peek = 1; current < collectionSize; current++, peek = current + 1) {
            boolean outOfBounds = peek == collectionSize;
            consumer.accept(collection.get(current), outOfBounds ? null : collection.get(peek));
        }
    }


}
