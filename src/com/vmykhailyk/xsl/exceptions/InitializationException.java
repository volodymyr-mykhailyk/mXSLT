package com.vmykhailyk.xsl.exceptions;

/**
 * User: Volodymyr.Mykhailyk
 * Date: Dec 10, 2010
 * Time: 5:11:40 PM
 */
public class InitializationException extends MException {
    public InitializationException(String s) {
        super(s);
    }

    public InitializationException(String s, Exception e) {
        super(s, e);
    }
}
