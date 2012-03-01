package com.vmykhailyk.xsl.modules.extension;

import com.vmykhailyk.xsl.modules.TaskCompiler;
import com.vmykhailyk.xsl.utils.Constants;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

import java.io.File;
import java.io.IOException;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 11.03.11
 * Time: 19:10
 */
public class SpawnNewThreadFunction extends AbstractXSLTFunction {
    private FunctionImpl function = new FunctionImpl();
    private TaskCompiler compiler;

    public SpawnNewThreadFunction(TaskCompiler theCompiler) {
        super(Constants.SPAWN_NEW_THREAD_FUNC_NAME);
        compiler = theCompiler;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        SequenceType[] sequenceTypes = new SequenceType[2];
        sequenceTypes[0] = SequenceType.SINGLE_NODE;
        sequenceTypes[1] = SequenceType.SINGLE_STRING;
        return sequenceTypes;
    }

    @Override
    public SequenceType getResultType(SequenceType[] sequenceTypes) {
        return SequenceType.SINGLE_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return function;
    }

    private class FunctionImpl extends ExtensionFunctionCall {
        @Override
        public SequenceIterator call(SequenceIterator[] sequenceIterators, XPathContext xPathContext) throws XPathException {
            String name = ExtensionFunctionUtils.getStringParameter(sequenceIterators, 1).trim();
            SequenceIterator argument1 = sequenceIterators[0];
            NodeInfo info = (NodeInfo) argument1.next();
            XdmNode context = new XdmNode(info);
            File file = getFile(name);
            compiler.addTask(file, context);


            return SingletonIterator.makeIterator(new StringValue(getPartMarkerId(file.getAbsolutePath())));
        }

        private String getPartMarkerId(String name) {
            return "#mXSLT#" + name + "#mXSLT#";
        }

        private File getFile(String counter) {
            try {
                return File.createTempFile("mXSLT_", "_documentPart");
            } catch (IOException e) {
                logger.warn("Cannot create file in temp folder: " + e.getMessage());
                return new File("output/part" + counter + ".xml");
            }
        }
    }
}
