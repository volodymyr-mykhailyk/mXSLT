package com.vmykhailyk.xsl.data;

import com.vmykhailyk.xsl.exceptions.InitializationException;
import net.sf.saxon.trans.CommandLineOptions;

import java.io.File;
import java.io.IOException;


/**
 * Created by Volodymyr.Mykhailyk
 * Date: 16.02.11
 * Time: 13:09
 */
public class MConfig {
    private File inputFile;
    private File outputFile;
    private File xslFile;
    private int threadsCount;
    private File tempFile;
    private CommandLineOptions options;

    protected MConfig(String inputPath, String outputPath, String xslPath) {
        inputFile = new File(inputPath);
        outputFile = new File(outputPath);
        xslFile = new File(xslPath);
        threadsCount = Runtime.getRuntime().availableProcessors();
        createTempFile();
    }

    public static MConfig create(String inputPath, String outputPath, String xslPath) throws InitializationException {
        validateParameters(inputPath, outputPath, xslPath);
        return new MConfig(inputPath, outputPath, xslPath);
    }

    public static MConfig create(CommandLineOptions options) throws InitializationException {
        String inputPath = options.getOptionValue("s");
        String outputPath = options.getOptionValue("o");
        String xslFile = options.getOptionValue("xsl");
        MConfig mConfig = create(inputPath, outputPath, xslFile);
        mConfig.setCommandLineOptions(options);
        return mConfig;
    }

    private static void validateParameters(String inputPath, String outputPath, String xslPath) throws InitializationException {
        if (inputPath == null) {
            throw new InitializationException("Input path is not specified");
        }
        if (outputPath == null) {
            throw new InitializationException("Output path is not specified");
        }
        if (xslPath == null) {
            throw new InitializationException("Xsl path is not specified");
        }
    }

    private void createTempFile() {
        try {
            tempFile = File.createTempFile("mXSLT_", "_documentPart");
        } catch (IOException e) {
            tempFile = new File("output/root.xml");
        }
    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public File getXslFile() {
        return xslFile;
    }

    public int getThreadsCount() {
        return threadsCount;
    }

    public void setThreadsCount(int threadsCount) {
        this.threadsCount = threadsCount;
    }

    public File getTempOutputFile() {
        return tempFile;
    }

    public String getSaxonConfigFile() {
        return options != null ? options.getOptionValue("config") : null;
    }

    public void setCommandLineOptions(CommandLineOptions commandLineOptions) {
        this.options = commandLineOptions;
    }
}
