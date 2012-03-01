package com.vmykhailyk.xsl.modules;

import com.vmykhailyk.xsl.data.MConfig;
import com.vmykhailyk.xsl.exceptions.InitializationException;
import com.vmykhailyk.xsl.modules.loggers.XSLTErrorListener;
import com.vmykhailyk.xsl.modules.loggers.XSLTMessageLogger;
import net.sf.saxon.Configuration;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.s9api.*;
import net.sf.saxon.trans.XPathException;
import org.apache.log4j.Logger;

import javax.xml.transform.stream.StreamSource;
import java.io.File;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 14.02.11
 * Time: 12:46
 */
public class XSLTTransformerBuilder {
    private String id;
    private XSLTExtensionsFactory extensionsBuilder;
    private File xslFile;
    private Processor processor;
    private MConfig config;

    private static final Logger logger = Logger.getLogger(ProcessingTask.class);

    public XSLTTransformerBuilder(MConfig theConfig, TaskCompiler compiler) {
        config = theConfig;
        xslFile = config.getXslFile();
        id = xslFile.getAbsolutePath();
        extensionsBuilder = new XSLTExtensionsFactory(compiler);
        processor = new Processor(getSaxonConfiguration());
    }

    private Configuration getSaxonConfiguration() {
        String configFile = config.getSaxonConfigFile();
        if (configFile != null) {
            try {
                return Configuration.readConfiguration((new StreamSource(configFile)));
            } catch (XPathException e) {
                logger.error("Cannot read saxon configuration file. Using default: " + e.getMessage());
            }
        }
        return new Configuration();
    }


    public XsltTransformer getTransformer() throws InitializationException {
        try {

            XsltCompiler xsltCompiler = getCompiler();
            return compileTransformer(xsltCompiler);
        } catch (IllegalArgumentException e) {
            throw new InitializationException("Cannot read XSLT transformation file: " + getTransformationPath(), e);
        }
    }

    private XsltTransformer compileTransformer(XsltCompiler xsltCompiler) throws InitializationException {
        try {
            XsltExecutable executable = xsltCompiler.compile(new StreamSource(xslFile));
            XsltTransformer xsltTransformer = executable.load();
            xsltTransformer.setMessageListener(new XSLTMessageLogger(id));
            xsltTransformer.setErrorListener(XSLTErrorListener.getTransformationLogger(id));
            return xsltTransformer;
        } catch (SaxonApiException e) {
            throw new InitializationException("Cannot initialize XSLT transformer: " + getTransformationPath(), e);
        }
    }

    private XsltCompiler getCompiler() throws InitializationException {
        addExtensions(processor);

        XsltCompiler xsltCompiler = processor.newXsltCompiler();
        xsltCompiler.setErrorListener(XSLTErrorListener.getCompilationLogger(id));

        return xsltCompiler;
    }

    private void addExtensions(Processor processor) throws InitializationException {
        ExtensionFunctionDefinition[] extensions = extensionsBuilder.createExtensions();
        for (ExtensionFunctionDefinition extension : extensions) {
            processor.registerExtensionFunction(extension);
        }
    }

    private String getTransformationPath() {
        return xslFile.getAbsolutePath();
    }
}
