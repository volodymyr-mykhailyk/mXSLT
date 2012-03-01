package com.vmykhailyk.xsl.io;

import com.vmykhailyk.xsl.data.MConfig;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 25.11.11
 * Time: 17:54
 */
public class DataMerger {
    private MConfig config;
    private BufferedWriter writer;
    private Matcher matcher;

    public DataMerger(MConfig theConfig) {
        Pattern pattern = Pattern.compile("#mXSLT#(.+?)#mXSLT#");
        matcher = pattern.matcher("");
        config = theConfig;
    }

    public void mergeData() {
        try {
            createDirs(config.getOutputFile());
            writer = new BufferedWriter(new FileWriter(config.getOutputFile()));
            readData(config.getTempOutputFile());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDirs(File file) {
        String[] split = file.getAbsolutePath().split("\\\\");
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            String s = split[i];
            buffer.append(s);
            buffer.append("\\");
        }
        File dirs = new File(buffer.toString());
        dirs.mkdirs();
    }

    private void readData(File file) throws IOException {
        DataReader dataReader = new DataReader(file);
        dataReader.start();
        while (dataReader.hasData()) {
            String line = dataReader.getData();
            appendData(line);
            appendData("\n");
        }
        dataReader.stop();
    }

    private void appendData(String line) throws IOException {
        matcher.reset(line);
        if (matcher.find()) {
            writer.write(line.substring(0, matcher.start()));
            String fileName = matcher.group(1);
            String restOfLine = line.substring(matcher.end());
            readData(new File(fileName));
            appendData(restOfLine);
        } else {
            writer.write(line);
        }
    }
}
