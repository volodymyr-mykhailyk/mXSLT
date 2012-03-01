package com.vmykhailyk.xsl.performance;

import com.vmykhailyk.xsl.XSLTDebugger;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 25.11.11
 * Time: 15:35
 */
public class SSaxonTester implements RunnableComponent {
    @Override
    public void run(String input, String output, String xsl) {
        XSLTDebugger debugger = new XSLTDebugger(input, output, xsl);
        debugger.run();
    }
}
