package com.vmykhailyk.xsl.utils;

import java.util.HashMap;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 11.02.11
 * Time: 15:46
 */
public class PerformanceAnalyzer {
    private static HashMap<String, Double> results = new HashMap<String, Double>();
    public static boolean isEnabled = false;
    private String name;
    private long start = 0;

    public static void addStatistic(String comonent, Double nanoSecondsTime) {
        if (isEnabled) {
            Double converted = nanoSecondsTime / 1000000.0;
            Double previousResult = results.get(comonent);
            if (previousResult == null) {
                results.put(comonent, converted);
            } else {
                results.put(comonent, (previousResult + converted) / 2);
            }
        }
    }

    public static String getStatistic() {
        StringBuffer buffer = new StringBuffer();
        for (String component : results.keySet()) {
            buffer.append(String.format("Component %s: average time: %.3f milseconds", component, results.get(component)));
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public static HashMap<String, Double> getRawStatistic() {
        return results;
    }

    public PerformanceAnalyzer(String theName) {
        name = theName;
    }

    public void start() {
        if (isEnabled) {
            start = System.nanoTime();
        }
    }

    public void end() {
        if (isEnabled) {
            long end = System.nanoTime();
            if (start != 0) {
                addStatistic(name, (double) (end - start));
                start = 0;
            }
        }
    }
}
