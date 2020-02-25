package com.atixlabs.monitor;

import com.atixlabs.exception.ParametersNotFound;
import com.atixlabs.message.Message;
import com.atixlabs.monitor.http.MonitorService;
import com.atixlabs.monitor.processor.MeasurementsProcessor;
import com.atixlabs.monitor.queue.MonitorQueue;
import com.atixlabs.monitor.socket.MonitorSocketServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
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
    public static final int HTTP_PORT = 8080;
    private static final long PERIODIC_DELAY = 30;

    public static void main(String[] args) throws IOException, ParametersNotFound {
        Monitor monitor = new Monitor();
        monitor.validateParameters(args);
        LOG.info("Init measurements processor");
        monitor.initMeasurementsProcessor(args);
        LOG.info("Starting HTTP server");
        new MonitorService().startHttpServer();
        LOG.info("Preparing socket to accept connections");
        new MonitorSocketServer().start();
    }

    private void initMeasurementsProcessor(String[] args) {
        BlockingQueue<Message> queue = MonitorQueue.getInstance().getQueue();
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(new MeasurementsProcessor(queue, new BigDecimal(args[0]), new BigDecimal(args[1])),
                        PERIODIC_DELAY, PERIODIC_DELAY, TimeUnit.SECONDS);
    }

    private void validateParameters(String[] args) throws ParametersNotFound {
        if(args.length < 2 || StringUtils.isBlank(args[0]) || StringUtils.isBlank(args[1])){
            LOG.error("Parameters M and S are not set. Please set arguments.");
            throw new ParametersNotFound();
        }
    }
}
