package com.atixlabs.monitor;

import com.atixlabs.message.Message;
import com.atixlabs.monitor.processor.MeasurementsProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author larstack
 */
public class Monitor {

    private static final Logger LOG = LogManager.getLogger(Monitor.class);

    public static final int PORT = 2222;
    private static final int THREADS = 8;
    private static final long PERIODIC_DELAY = 30;
    public static final int HTTP_PORT = 8080;

    public static void main(String[] args) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        Monitor monitor = new Monitor();
        LOG.info("Init measurements processor");
        monitor.initMeasurementsProcessor(args);
        LOG.info("Starting HTTP server");
        new MonitorService().startHttpServer(executor);
        LOG.info("Preparing socket to accept connections");
        new MonitorSocketServer(executor).start();
    }

    private void initMeasurementsProcessor(String[] args) {
        BlockingQueue<Message> queue = MonitorQueue.getInstance().getQueue();
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(new MeasurementsProcessor(queue, new BigDecimal(args[0]), new BigDecimal(args[1])),
                        PERIODIC_DELAY, PERIODIC_DELAY, TimeUnit.SECONDS);
    }


}
