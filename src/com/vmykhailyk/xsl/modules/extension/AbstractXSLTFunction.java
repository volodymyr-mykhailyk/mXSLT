package com.vmykhailyk.xsl.modules.extension;

import com.vmykhailyk.xsl.utils.Constants;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.StructuredQName;
import org.apache.log4j.Logger;

public abstract class AbstractXSLTFunction extends ExtensionFunctionDefinition {
    protected String name;
    protected Logger logger;

    public AbstractXSLTFunction(String name) {
        this.name = name;
        this.logger = Logger.getLogger(this.name);
    }

    @Override
    public StructuredQName getFunctionQName() {
        return new StructuredQName(Constants.NS_PREFIX, Constants.NS_URL, name);
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return Constants.NS_PREFIX + ":" + name;
    }
}
