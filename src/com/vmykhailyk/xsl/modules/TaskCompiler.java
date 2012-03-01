package com.vmykhailyk.xsl.modules;

import net.sf.saxon.s9api.XdmNode;
import org.apache.commons.pool.ObjectPool;
import org.apache.log4j.Logger;

import javax.xml.transform.Source;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by Volodymyr.Mykhailyk
 * Date: 24.11.11
 * Time: 15:29
 */
public class TaskCompiler {
    private ExecutorService executor;
    private ObjectPool processors;
    private LinkedBlockingQueue<Future> results;
    private static final Logger logger = Logger.getLogger(TaskCompiler.class);

    public void initExecutor(ExecutorService processingExecutor, LinkedBlockingQueue<Future> resultsQueue) {
        executor = processingExecutor;
        results = resultsQueue;
    }

    public void initProcessors(ObjectPool processorsPool) {
        processors = processorsPool;
    }

    public void addInitialTask(File outputFile, Source input) {
        ProcessingTask task = compileProcessingTask(outputFile);
        task.setSource(input);
        submit(task);
    }

    private void submit(ProcessingTask task) {
        try {
            Future future = executor.submit(task);
            results.put(future);
        } catch (InterruptedException e) {
            logger.error("Processing interrupted. Cannot add task.", e);
        } catch (RejectedExecutionException e) {
            logger.error("Task rejected. Incorrect executor setup.", e);
        }
    }

    private ProcessingTask compileProcessingTask(File file) {
        return new ProcessingTask(file, processors);
    }

    public void addTask(File file, XdmNode context) {
        ProcessingTask task = compileProcessingTask(file);
        task.setContextNode(context);
        submit(task);
    }
}
