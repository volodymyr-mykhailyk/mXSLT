package com.vmykhailyk.xsl;

import com.vmykhailyk.xsl.utils.Constants;
import com.vmykhailyk.xsl.utils.PerformanceAnalyzer;
import net.sf.saxon.Configuration;
import net.sf.saxon.s9api.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 23.11.11
 * Time: 17:01
 */
public class XSLTDebugger {
    private DocumentBuilder documentBuilder;
    private String xslPath;
    private String inputPath;
    private String outputPath;

    public XSLTDebugger(String input, String output, String xsl) {
        try {
            inputPath = input;
            outputPath = output;
            xslPath = "C:\\projects\\other\\mSaxon\\xsl\\xMark\\xMark.xsl";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            documentBuilder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String input = Constants.INPUT_FILE;
        String output = Constants.OUTPUT_FILE;
        String xsl = "C:\\projects\\other\\mSaxon\\xsl\\xMark\\xMark.xsl";

        for (int i = 0; i < Constants.REPEAT_COUNT; i++) {
            System.out.println("Iteration: "+i);
            PerformanceAnalyzer.isEnabled = true;
            PerformanceAnalyzer analyzer = new PerformanceAnalyzer("XSLTDebugger");
            analyzer.start();
            XSLTDebugger debugger = new XSLTDebugger(input, output, xsl);
            debugger.run();
            analyzer.end();
            System.out.println(PerformanceAnalyzer.getStatistic());
        }
    }

    public void run() {
        try {
            Processor processor = new Processor(true);
            Configuration configuration = processor.getUnderlyingConfiguration();
            XsltCompiler compiler = processor.newXsltCompiler();
            XsltTransformer transformer = compileTransformer(compiler, xslPath);
            transformer.setSource(new StreamSource(new File(inputPath)));
            transformer.setDestination(getSerializer());
            transformer.transform();
            transformer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Serializer getSerializer() {
        return new Serializer(new File(outputPath));
    }

    private XsltTransformer compileTransformer(XsltCompiler xsltCompiler, String transformationDocument) throws SaxonApiException {
        XsltExecutable executable = xsltCompiler.compile(new StreamSource(transformationDocument));
        return executable.load();
    }

    private Document getDocument(String path) throws IOException, SAXException {
        return this.documentBuilder.parse(path);
    }
}
