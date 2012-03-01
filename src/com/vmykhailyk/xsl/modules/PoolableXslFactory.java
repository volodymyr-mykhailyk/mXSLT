package com.vmykhailyk.xsl.modules;

import com.vmykhailyk.xsl.data.MConfig;
import net.sf.saxon.s9api.XsltTransformer;
import org.apache.commons.pool.PoolableObjectFactory;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 18.02.11
 * Time: 13:20
 */
public class PoolableXslFactory implements PoolableObjectFactory {
    private XSLTTransformerBuilder builder;

    public PoolableXslFactory(MConfig theConfiguration, TaskCompiler compiler) {
        builder = new XSLTTransformerBuilder(theConfiguration, compiler);
    }

    @Override
    public Object makeObject() throws Exception {
        return builder.getTransformer();
    }

    @Override
    public void destroyObject(Object o) throws Exception {
        XsltTransformer transformer = convert(o);
        transformer.close();
    }

    @Override
    public boolean validateObject(Object o) {
        return true;
    }

    @Override
    public void activateObject(Object o) throws Exception {
    }

    @Override
    public void passivateObject(Object o) throws Exception {
    }

    private XsltTransformer convert(Object chain) {
        return (XsltTransformer) chain;
    }
}
