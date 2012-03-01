package com.vmykhailyk.xsl.modules.resolvers;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/**
 * User: Volodymyr.Mykhailyk
 * Date: 1/26/11
 * Time: 5:44 PM
 */
public class XSLTFilesResolver implements URIResolver {
    protected static Logger logger = Logger.getLogger(XSLTFilesResolver.class);
    private DocumentBuilder documentBuilder;
    private String basePath;

    public XSLTFilesResolver(DocumentBuilder documentBuilder, String basePath) {
        this.documentBuilder = documentBuilder;
        this.basePath = basePath;
    }

    public Source resolve(String href, String base) throws TransformerException {
        logger.debug("resolving: " + basePath + href);
        InputStream transformationInputStream = this.getClass().getResourceAsStream(normalizePath(basePath + href));
        Document config = null;
        try {
            config = documentBuilder.parse(transformationInputStream);
        } catch (SAXException e) {
            logger.error("Cannot resolve href: " + href + ". Error: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Cannot resolve href: " + href + ". Error: " + e.getMessage());
        }
        return new DOMSource(config);
    }

    protected String normalizePath(String path) {
        path = path.replaceAll("\\\\", "/");
        if (!path.contains("/../")) {
            return path;
        } else {
            return stripRelativeDirs(path);
        }
    }

    private String stripRelativeDirs(String path) {
        String[] strings = path.split("/");
        Stack<String> list = new Stack<String>();
        for (String token : strings) {
            if (token.isEmpty()) {
                continue;
            }

            if (token.equals("..")) {
                list.pop();
            } else {
                list.push(token);
            }
        }

        return joinStringArray(list);
    }

    private String joinStringArray(Stack<String> list) {
        StringBuffer result = new StringBuffer();
        for (String token : list) {
            result.append("/");
            result.append(token);
        }
        return result.toString();
    }

}
