package com.vmykhailyk.xsl.exceptions;


/**
 * User: Volodymyr.Mykhailyk
 * Date: Dec 10, 2010
 * Time: 4:09:52 PM
 */
public class ProcessingException extends MException {
    public ProcessingException(String s) {
        super(s);
    }

    public ProcessingException(String s, Throwable e) {
        super(s, e);
    }
}
