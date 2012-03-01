package com.vmykhailyk.xsl.modules;

import com.vmykhailyk.xsl.exceptions.InitializationException;
import com.vmykhailyk.xsl.modules.extension.PerformanceAnalyzerFunction;
import com.vmykhailyk.xsl.modules.extension.SpawnNewThreadFunction;
import net.sf.saxon.lib.ExtensionFunctionDefinition;

import java.util.ArrayList;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 20.07.11
 * Time: 18:55
 */
public class XSLTExtensionsFactory {
    private TaskCompiler compiler;

    public XSLTExtensionsFactory(TaskCompiler compiler) {
        this.compiler = compiler;
    }

    public ExtensionFunctionDefinition[] createExtensions() throws InitializationException {
        ArrayList<ExtensionFunctionDefinition> extensions = new ArrayList<ExtensionFunctionDefinition>();
        extensions.add(new PerformanceAnalyzerFunction());
        extensions.add(new SpawnNewThreadFunction(compiler));

        return extensions.toArray(new ExtensionFunctionDefinition[extensions.size()]);
    }
}
