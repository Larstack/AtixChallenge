package com.atixlabs.monitor.http;

import com.atixlabs.message.Message;
import com.atixlabs.monitor.queue.MonitorQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.concurrent.BlockingQueue;

import static com.atixlabs.monitor.Monitor.HTTP_PORT;

/**
 * @author larstack
 */
@Path("monitor")
public class MonitorService {

    private static final Logger LOG = LogManager.getLogger(MonitorService.class);
    private static final String API_PACKAGE = "com.atixlabs.monitor.http";

    private final BlockingQueue<Message> queue;

    public MonitorService() {
        super();
        queue = MonitorQueue.getInstance().getQueue();
    }

    @POST
    @Path("/measurement")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postMeasurement(Message message) {
        LOG.info("Measurement received by HTTP");
        try {
            queue.put(message);
            LOG.debug("Message [" + message + "] inserted to the queue");
        } catch (Exception e) {
            LOG.error("Error ocurred inserting message to the queue");
            e.printStackTrace();
        }
        return Response.ok(message).build();
    }

    public void startHttpServer() {
        ResourceConfig rc = new ResourceConfig().packages(API_PACKAGE);
        GrizzlyHttpServerFactory.createHttpServer(URI.create("http://localhost:"+HTTP_PORT+"/"), rc);
        LOG.info("HTTP server ready on port " + HTTP_PORT);
    }
}
