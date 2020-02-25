package com.atixlabs.monitor;

import com.atixlabs.message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static com.atixlabs.monitor.Monitor.PORT;

/**
 * @author larstack
 */
public class MonitorSocketServer {

    private static final Logger LOG = LogManager.getLogger(MonitorSocketServer.class);

    private final ExecutorService executor;

    public MonitorSocketServer(ExecutorService executor) {
        this.executor = executor;
    }

    public void start() throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        BlockingQueue<Message> queue = MonitorQueue.getInstance().getQueue();

        LOG.info("Socket ready to accept connection");

        while (true){
            Socket socket = server.accept();
            executor.execute(() -> {
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    Message input = (Message) inputStream.readObject();
                    queue.put(input);
                    LOG.debug("Message [" + input + "] inserted to the queue");
                    inputStream.close();
                } catch (Exception e) {
                    LOG.error("Error ocurred inserting message to the queue");
                    e.printStackTrace();
                }
            });
        }
    }
}
