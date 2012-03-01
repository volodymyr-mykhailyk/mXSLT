package com.vmykhailyk.xsl.performance;

import com.vmykhailyk.xsl.MSaxon;
import com.vmykhailyk.xsl.data.MConfig;
import com.vmykhailyk.xsl.exceptions.MException;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 25.11.11
 * Time: 15:34
 */
public class MSaxonTester implements RunnableComponent{
    private int threadsCount;

    public MSaxonTester(int threads) {
        threadsCount = threads;
    }

    @Override
    public void run(String input, String output, String xsl) {
        try {
            MConfig config = MConfig.create(input, output, xsl);
            config.setThreadsCount(threadsCount);
            MSaxon mSaxon = new MSaxon(config);
            mSaxon.initialize();
            mSaxon.execute();
            mSaxon.teardown();
        } catch (MException e) {
            e.printStackTrace();
        }
    }
}