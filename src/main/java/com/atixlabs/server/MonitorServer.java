package com.atixlabs.server;

import com.atixlabs.message.Message;
import com.atixlabs.server.task.MeasurementsProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author larstack
 */
public class MonitorServer {

    private static final Logger LOG = LogManager.getLogger(MonitorServer.class);

    private static final int PORT = 5000;
    private static final int THREADS = 2;
    private static final int QUEUE_CAPACITY = 60;
    private static final long PERIODIC_DELAY = 30;

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(PORT);
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        BlockingQueue<Message> queue = new ArrayBlockingQueue(QUEUE_CAPACITY, true);

        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(new MeasurementsProcessor(queue, new BigDecimal(args[0]), new BigDecimal(args[1])),
                        0, PERIODIC_DELAY, TimeUnit.SECONDS);

        while (true){
            LOG.info("Waiting for request");
            Socket socket = server.accept();
            executor.execute(() -> {
                try {
                    LOG.info("New client connected");
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    Message input = (Message) inputStream.readObject();
                    LOG.debug("Message: [" + input + "]");
                    queue.put(input);
                    LOG.debug("Message inserted to the queue");
                } catch (Exception e) {
                    LOG.error("Error ocurred inserting message to the queue");
                    e.printStackTrace();
                }
            });
        }
    }
}
