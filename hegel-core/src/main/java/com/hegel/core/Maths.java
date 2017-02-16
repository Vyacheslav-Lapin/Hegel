package com.hegel.core;

import java.util.Arrays;

public interface Maths {
    static int max(int... ints) {
        return Arrays.stream(ints)
                .reduce(Integer.MIN_VALUE,
                        (i, j) -> i < j ? j : i);
    }
}
