package com.vmykhailyk.xsl.modules.extension;

import com.vmykhailyk.xsl.utils.Constants;
import com.vmykhailyk.xsl.utils.PerformanceAnalyzer;
import net.sf.saxon.expr.StaticProperty;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.type.BuiltInAtomicType;
import net.sf.saxon.value.BooleanValue;
import net.sf.saxon.value.SequenceType;

import java.util.HashMap;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 21.02.11
 * Time: 14:05
 */
public class PerformanceAnalyzerFunction extends AbstractXSLTFunction {
    private static HashMap<String, Long> components = new HashMap<String, Long>();

    public PerformanceAnalyzerFunction() {
        super(Constants.PERFORMANCE_ANALYZER_XSLT_FUNC);
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 1;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return ExtensionFunctionUtils.getStringType();
    }

    @Override
    public SequenceType getResultType(SequenceType[] sequenceTypes) {
        return SequenceType.makeSequenceType(BuiltInAtomicType.BOOLEAN, StaticProperty.ALLOWS_ONE);
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new FunctionImpl();
    }

    private class FunctionImpl extends ExtensionFunctionCall {
        private boolean isEnabled;
        private SequenceIterator result = null;

        private FunctionImpl() {
            isEnabled = PerformanceAnalyzer.isEnabled;
        }

        @Override
        public SequenceIterator call(SequenceIterator[] arguments, XPathContext xPathContext) throws XPathException {
            if (isEnabled) {
                String componentName = ExtensionFunctionUtils.getStringParameter(arguments, 0);

                long now = System.nanoTime();
                Long start = components.get(componentName);
                if (start != null) {
                    PerformanceAnalyzer.addStatistic(componentName, (double) now - start);
                    components.remove(componentName);
                } else {
                    components.put(componentName, now);
                }
            }
            return isEnabled ? SingletonIterator.makeIterator(BooleanValue.TRUE) : SingletonIterator.makeIterator(BooleanValue.FALSE);
        }
    }
}
