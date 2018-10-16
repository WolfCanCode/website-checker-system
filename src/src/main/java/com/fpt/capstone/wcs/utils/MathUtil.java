package com.fpt.capstone.wcs.utils;

import java.util.List;

public class MathUtil {

    public Double calculateSumDoubleList(List<Double> list) {
        double sum = 0;
        for (Double num : list) {
            sum += num;
        }
        return sum;
    }
}
