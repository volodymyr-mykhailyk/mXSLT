package com.vmykhailyk.xsl.performance;

import com.vmykhailyk.xsl.utils.Constants;
import com.vmykhailyk.xsl.utils.PerformanceAnalyzer;
import org.apache.log4j.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 25.11.11
 * Time: 15:30
 */
public class PerformanceTester {
    public static void main(String[] args) throws InterruptedException, IOException {
        PerformanceTester tester = new PerformanceTester();
        tester.run();
    }

    private void run() throws InterruptedException, IOException {
        Logger logger = Logger.getRootLogger();
        logger.setLevel(Level.ERROR);
        BasicConfigurator.configure();
        logger.addAppender(new FileAppender(new PatternLayout("%-5p %c{1} - %m%n"), "results.log", false));


        HashMap<String, RunnableComponent> components = getComponents();
        String[] files = getFiles();
        warmUp(components, "C:\\projects\\other\\mSaxon\\input\\xMark\\out00.xml");


        PerformanceAnalyzer.isEnabled = true;
        for (String file : files) {
            for (String componentName : components.keySet()) {
                RunnableComponent component = components.get(componentName);
                String name = componentName + "\t" + new File(file).getName();
                PerformanceAnalyzer analyzer = new PerformanceAnalyzer(name);
                for (int i = 0; i < Constants.REPEAT_COUNT; i++) {
                    logger.error("Iteration: " + name + ": " + i);
                    analyzer.start();
                    runComponent(component, file);
                    analyzer.end();
                    Thread.sleep(200);
                }
                logger.error(printStatistic(PerformanceAnalyzer.getRawStatistic()));
//                logger.error("\n" + PerformanceAnalyzer.getStatistic());

            }
        }
        logger.error(printStatistic(PerformanceAnalyzer.getRawStatistic()));
    }

    private String printStatistic(HashMap<String, Double> statistic) {
        HashMap<String, HashMap<String, Double>> map = convertRawStatistic(statistic);

        StringBuilder builder = new StringBuilder(getHeader());
        for (String component : getSortedKeys(map)) {
            builder.append(component);
            builder.append("\t");
            HashMap<String, Double> timeMap = map.get(component);
            for (String file : getSortedKeys(timeMap)) {
                builder.append(String.format("%.3f", timeMap.get(file)));
                builder.append("\t");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private <T> ArrayList<String> getSortedKeys(HashMap<String, T> map) {
        Set<String> strings = map.keySet();
        ArrayList<String> list = new ArrayList<String>(strings);
        Collections.sort(list);
        return list;
    }

    private String getHeader() {
        StringBuilder builder = new StringBuilder("\nComponent\t");
        String[] files = getFiles();
        for (String file : files) {
            builder.append(new File(file).getName());
            builder.append("\t");
        }
        builder.append("\nComponent\t");
        for (String file : files) {
            long space = new File(file).length();
            builder.append(String.format("%.3f", (double)space/1024/1024));
            builder.append("\t");
        }
        builder.append("\n");

        return builder.toString();
    }

    private HashMap<String, HashMap<String, Double>> convertRawStatistic(HashMap<String, Double> statistic) {
        HashMap<String, HashMap<String, Double>> map = new HashMap<String, HashMap<String, Double>>();
        for (String component : statistic.keySet()) {
            String[] strings = component.split("\t");
            String componentName = strings[0];
            String fileName = strings[1];
            HashMap<String, Double> timeMap = map.get(componentName);
            if (timeMap == null) {
                timeMap = new HashMap<String, Double>();
                map.put(componentName, timeMap);
            }
            timeMap.put(fileName, statistic.get(component));
        }
        return map;
    }

    private void runComponent(RunnableComponent component, String file) {
        component.run(file, Constants.OUTPUT_FILE, Constants.XSL_FILE);
    }

    private void warmUp(HashMap<String, RunnableComponent> components, String file) {
        for (RunnableComponent component : components.values()) {
            runComponent(component, file);
        }
    }

    private static String[] getFiles() {
        return new String[]{
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out00.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out01.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out02.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out03.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out04.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out05.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out06.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out07.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out08.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out09.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out10.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out11.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out12.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out13.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out14.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out15.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out16.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out17.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out18.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out19.xml",
                "C:\\projects\\other\\mSaxon\\input\\xMark\\out20.xml",
        };
    }

    private static HashMap<String, RunnableComponent> getComponents() {
        HashMap<String, RunnableComponent> componentHashMap = new HashMap<String, RunnableComponent>();
        componentHashMap.put("SSaxonTester", new SSaxonTester());
//        componentHashMap.put("MSaxonTester2", new MSaxonTester(2));
//        componentHashMap.put("MSaxonTester3", new MSaxonTester(3));
        componentHashMap.put("MSaxonTester4", new MSaxonTester(4));
        return componentHashMap;
    }
}
