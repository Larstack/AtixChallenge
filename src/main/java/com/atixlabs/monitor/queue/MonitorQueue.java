package com.atixlabs.monitor.queue;

import com.atixlabs.message.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author larstack
 */
public class MonitorQueue {

    private static final int QUEUE_CAPACITY = 60;
    private static MonitorQueue instance;
    private BlockingQueue<Message> queue;

    private MonitorQueue() {
        queue = new ArrayBlockingQueue(QUEUE_CAPACITY, true);
    }

    public synchronized static MonitorQueue getInstance() {
        if (instance == null) {
            instance = new MonitorQueue();
        }
        return instance;
    }

    public BlockingQueue<Message> getQueue() {
        return queue;
    }
}
