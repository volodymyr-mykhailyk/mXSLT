package com.vmykhailyk.xsl.exceptions;

/**
 * User: Volodymyr.Mykhailyk
 * Date: Dec 10, 2010
 * Time: 5:11:54 PM
 */
public class MException extends Exception {
    public MException(String s) {
        super(s);
    }

    public MException(String s, Throwable e) {
        super(s, e);
    }
}
