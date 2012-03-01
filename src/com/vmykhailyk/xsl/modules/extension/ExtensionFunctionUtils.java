package com.vmykhailyk.xsl.modules.extension;

import net.sf.saxon.expr.StaticProperty;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.BuiltInAtomicType;
import net.sf.saxon.value.SequenceType;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: Jul 22, 2010
 * Time: 1:30:55 PM
 */
public class ExtensionFunctionUtils {

    public static int getIntegerParameter(SequenceIterator[] arguments, int position) throws XPathException {
        if (position < arguments.length) {
            SequenceIterator argument1 = arguments[position];
            return Integer.parseInt(argument1.next().getStringValue());
        }

        return 0;
    }

    public static String getStringParameter(SequenceIterator[] arguments, int position) throws XPathException {
        return ExtensionFunctionUtils.getStringParameter(arguments, position, null);
    }

    public static String getStringParameter(SequenceIterator[] arguments, int position, String defaultValue) throws XPathException {
        if (position < arguments.length) {
            SequenceIterator argument1 = arguments[position];
            return argument1.next().getStringValue();
        }

        return defaultValue;
    }

    public static SequenceType[] getStringType() {
        SequenceType[] types = new SequenceType[1];
        types[0] = SequenceType.makeSequenceType(BuiltInAtomicType.STRING, StaticProperty.ALLOWS_ONE);
        return types;
    }


}
