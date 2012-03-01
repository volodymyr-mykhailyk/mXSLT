package com.vmykhailyk.xsl;

import com.vmykhailyk.xsl.io.DataMerger;
import com.vmykhailyk.xsl.modules.PoolableXslFactory;
import com.vmykhailyk.xsl.data.MConfig;
import com.vmykhailyk.xsl.exceptions.InitializationException;
import com.vmykhailyk.xsl.exceptions.ProcessingException;
import com.vmykhailyk.xsl.modules.TaskCompiler;
import com.vmykhailyk.xsl.utils.Constants;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import javax.xml.transform.stream.StreamSource;
import java.util.concurrent.*;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 24.11.11
 * Time: 12:23
 */
public class MSaxon {
    private MConfig config;
    private PoolableXslFactory factory;
    private ObjectPool processorsPool;
    private ThreadPoolExecutor processingExecutor;
    private TaskCompiler compiler;

    private static final Logger logger = Logger.getLogger(MSaxon.class);
    private LinkedBlockingQueue<Future> resultsQueue;


    public MSaxon(MConfig theConfig) {
        config = theConfig;
        compiler = new TaskCompiler();
        factory = new PoolableXslFactory(config, compiler);
    }

    public void execute() throws ProcessingException {
        try {
            compiler.addInitialTask(config.getTempOutputFile(), new StreamSource(config.getInputFile()));
            while (resultsQueue.size() > 0) {
                Future future = resultsQueue.poll();
                future.get();
            }
            logger.info("Initial task is finished. Waiting for processors to finish the processing");
            processingExecutor.shutdown();
            processingExecutor.awaitTermination(Constants.TERMINATION_TIMEOUT, TimeUnit.SECONDS);
            logger.info("Processing is finished.");

            DataMerger merger = new DataMerger(config);
            merger.mergeData();

        } catch (InterruptedException e) {
            logger.error("Transformation was interrupted: " + e.getMessage());
        } catch (ExecutionException e) {
            logger.error("Transformation exception: " + e.getMessage(), e);
        }
    }



    public void initialize() throws InitializationException {
        initializeProcessorsPool();
        createProcessorsExecutor();
        intiCompiler();
    }

    private void intiCompiler() {
        compiler.initExecutor(processingExecutor, resultsQueue);
        compiler.initProcessors(processorsPool);
    }

    private void initializeProcessorsPool() throws InitializationException {
        GenericObjectPool pool = new GenericObjectPool(factory);
        int threadsCount = config.getThreadsCount();
        pool.setMaxActive(threadsCount);
        processorsPool = pool;
    }

    private void createProcessorsExecutor() {
        int threadsCount = config.getThreadsCount();
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        resultsQueue = new LinkedBlockingQueue<Future>();
        processingExecutor = new ThreadPoolExecutor(threadsCount, threadsCount,
                0L, TimeUnit.MILLISECONDS,
                workQueue, new ThreadPoolExecutor.CallerRunsPolicy());
    }



    public void teardown() {
        processingExecutor.shutdownNow();
        try {
            processorsPool.close();
        } catch (Exception ignored) {}
    }
}
