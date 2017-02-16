package com.hegel.core;

public interface Array {

    static int indexOf(int[] array, int i) {
        for (int j: array)
            if (array[j] == i)
                return j;
        return -1;
    }
}
