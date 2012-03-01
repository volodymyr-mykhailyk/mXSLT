package com.vmykhailyk.xsl;

import com.vmykhailyk.xsl.data.MConfig;
import com.vmykhailyk.xsl.exceptions.MException;
import com.vmykhailyk.xsl.utils.PerformanceAnalyzer;
import net.sf.saxon.Transform;
import net.sf.saxon.trans.CommandLineOptions;
import net.sf.saxon.trans.XPathException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 24.11.11
 * Time: 12:23
 */
public class MXSLTRunner {
    public static void main(String[] args) {
        Logger logger = Logger.getRootLogger();
        logger.setLevel(Level.INFO);
        BasicConfigurator.configure();
        PerformanceAnalyzer.isEnabled = true;

        try {
            CommandLineOptions options = new CommandLineOptions();
            Transform transform = new Transform();
            transform.setPermittedOptions(options);
            options.setActualOptions(args);

            MConfig config = MConfig.create(options);
            printInputOptions(config, logger);
            MXSLT mXSLT = new MXSLT(config);
            mXSLT.initialize();
            mXSLT.execute();
            mXSLT.teardown();
        } catch (MException e) {
            e.printStackTrace();
        } catch (XPathException e) {
            quit("Saxon command line options incorrect: " + e.getMessage(), 2);
        }
    }

    private static void printInputOptions(MConfig config, Logger logger) {
        logger.info("Processing data:");
        logger.info("  input:  " + config.getInputFile());
        logger.info("  output: " + config.getOutputFile());
        logger.info("  xsl:    " + config.getXslFile());
    }

    protected static void quit(String message, int code) {
        System.err.println(message);
        System.exit(code);
    }
}
