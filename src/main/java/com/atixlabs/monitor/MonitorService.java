package com.atixlabs.monitor;

import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

import static com.atixlabs.monitor.Monitor.HTTP_PORT;

/**
 * @author larstack
 */
public class MonitorService {

    private static final Logger LOG = LogManager.getLogger(MonitorService.class);

    public HttpServer startHttpServer() {
        return startHttpServer(null);
    }

    public HttpServer startHttpServer(ExecutorService executor) {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
            server.setExecutor(executor);
            server.start();
            LOG.info("HTTP server ready");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return server;
    }

}
