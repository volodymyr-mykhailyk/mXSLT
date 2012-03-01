package com.vmykhailyk.xsl.modules.loggers;

import net.sf.saxon.s9api.MessageListener;
import net.sf.saxon.s9api.XdmNode;
import org.apache.log4j.Logger;

import javax.xml.transform.SourceLocator;

/**
 * User: Volodymyr.Mykhailyk
 * Date: 1/26/11
 * Time: 5:33 PM
 */
public class XSLTMessageLogger implements MessageListener {
    protected static Logger logger = Logger.getLogger(XSLTMessageLogger.class);
    private String id;

    public XSLTMessageLogger(String theId) {
        id = "[" + theId + "]: ";
    }

    @Override
    public void message(XdmNode xdmNode, boolean isTerminated, SourceLocator sourceLocator) {
        String message = xdmNode.getStringValue();
        if (isTerminated) {
            logger.error(id + message);
        }
        if (message.startsWith("[INFO]")) {
            logger.info(id + message.substring(6));
        } else if (message.startsWith("[WARN]")) {
            logger.warn(id + message.substring(6));
        } else if (message.startsWith("[ERROR]")) {
            logger.error(id + message.substring(7));
        } else {
            logger.debug(id + message);
        }
    }
}
