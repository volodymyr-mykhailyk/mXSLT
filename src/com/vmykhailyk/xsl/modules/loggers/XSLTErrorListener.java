package com.vmykhailyk.xsl.modules.loggers;

import org.apache.log4j.Logger;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 25.02.11
 * Time: 15:41
 */
public class XSLTErrorListener implements ErrorListener {
    private String message;
    protected static Logger logger = Logger.getLogger(XSLTErrorListener.class);

    protected XSLTErrorListener(String theMessage) {
        message = theMessage;
    }

    @Override
    public void warning(TransformerException exception) throws TransformerException {
        logger.warn(message + exception.getMessage());
    }

    @Override
    public void error(TransformerException exception) throws TransformerException {
        logger.error(message + exception.getMessage());
    }

    @Override
    public void fatalError(TransformerException exception) throws TransformerException {
        logger.error(message + exception.getMessage());
    }

    public static ErrorListener getTransformationLogger(String id) {
        return new XSLTErrorListener("Transformation [" + id + "]:");
    }

    public static ErrorListener getCompilationLogger(String id) {
        return new XSLTErrorListener("Compilation [" + id + "]: ");
    }
}
