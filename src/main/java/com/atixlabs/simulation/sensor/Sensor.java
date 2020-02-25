package com.atixlabs.simulation.sensor;

import com.atixlabs.message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import static com.atixlabs.server.MonitorServer.PORT;
import static com.atixlabs.utils.Utils.round;

/**
 * @author larstack
 */
public class Sensor extends Thread {

    private static final Logger LOG = LogManager.getLogger(Sensor.class);

    private String id;

    public Sensor(String id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            String host = InetAddress.getLocalHost().getHostName();
            for (int i=1; i<=30; i++){
                Socket socket = new Socket(host, PORT);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                String messageId = "SENSOR_" + id + "_MSG_" + i;
                Message message = new Message(round(Math.random()), messageId);
                oos.writeObject(message);
                LOG.debug("Message [" + messageId + "] sent to monitor");
                oos.close();
                socket.close();
                sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}