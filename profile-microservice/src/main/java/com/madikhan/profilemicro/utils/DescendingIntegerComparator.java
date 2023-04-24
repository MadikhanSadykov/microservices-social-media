package com.madikhan.profilemicro.utils;

import java.util.Comparator;

public class DescendingIntegerComparator implements Comparator<Integer> {

    public int compare(Integer lhs, Integer rhs) {
        // compare reversed
        return rhs.compareTo(lhs);
    }

}
