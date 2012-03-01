package com.vmykhailyk.xsl.modules;

import com.vmykhailyk.xsl.exceptions.ProcessingException;
import com.vmykhailyk.xsl.utils.Constants;
import net.sf.saxon.s9api.*;
import org.apache.commons.pool.ObjectPool;
import org.apache.log4j.Logger;

import javax.xml.transform.Source;
import java.io.File;
import java.util.concurrent.Callable;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 18.02.11
 * Time: 12:40
 */
public final class ProcessingTask implements Callable<Object> {
    private final File outputFile;
    private final ObjectPool processingPool;
    private static final Logger logger = Logger.getLogger(ProcessingTask.class);
    private XdmNode context = null;
    private Source source;

    public ProcessingTask(File theOutputFile, ObjectPool theProcessingPool) {
        outputFile = theOutputFile;
        processingPool = theProcessingPool;
    }

    @Override
    public Object call() throws Exception {
        try {
            String name = outputFile.getName();
            logger.info("Transform started: " + name);
            XsltTransformer transformer = (XsltTransformer) processingPool.borrowObject();
            try {
                execute(transformer);
                logger.info("Transform finished: " + name);
                return new Object();
            } finally {
                processingPool.returnObject(transformer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProcessingException("Cannot get processor from pool: " + e.getMessage(), e);
        }
    }

    private void execute(XsltTransformer transformer) throws SaxonApiException {
        try {
            if (context != null) {
                configureThreadTransformer(transformer);
            } else {
                configureMainTransformer(transformer);
            }
            transformer.transform();
        } catch (SaxonApiException e) {
            e.printStackTrace();
        }
    }

    private void configureMainTransformer(XsltTransformer transformer) throws SaxonApiException {
        transformer.setDestination(new Serializer(outputFile));
        transformer.setSource(source);
    }

    private void configureThreadTransformer(XsltTransformer transformer) throws SaxonApiException {
        Serializer serializer = new Serializer(outputFile);
        serializer.setOutputProperty(Serializer.Property.OMIT_XML_DECLARATION, "yes");
        serializer.setOutputProperty(Serializer.Property.DOCTYPE_PUBLIC, "");
        serializer.setOutputProperty(Serializer.Property.DOCTYPE_SYSTEM, "");
        transformer.setDestination(serializer);
        transformer.setInitialContextNode(context);
        transformer.setInitialTemplate(new QName(Constants.START_TEMPLATE_NAME));
    }

    public void setContextNode(XdmNode context) {
        this.context = context;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
