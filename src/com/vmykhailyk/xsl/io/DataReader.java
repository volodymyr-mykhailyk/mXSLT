package com.vmykhailyk.xsl.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 25.11.11
 * Time: 18:06
 */
public class DataReader {
    private File inputFile;
    private BufferedReader reader;
    private String line = null;

    public DataReader(File file) {
        inputFile = file;
    }


    public boolean hasData() {
        return line != null;
    }


    public String getData() throws IOException {
        String oldLine = line;
        line = reader.readLine();
        return oldLine;
    }

    public void stop() throws IOException {
        reader.close();
        inputFile.delete();
    }

    public void start() throws IOException {
        reader = new BufferedReader(new FileReader(inputFile));
        getData();
    }
}
