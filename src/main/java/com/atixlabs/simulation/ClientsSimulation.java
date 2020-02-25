package com.atixlabs.simulation;

import com.atixlabs.simulation.sensor.Sensor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.IntStream;

/**
 * @author larstack
 */
public class ClientsSimulation {

    private static final Logger LOG = LogManager.getLogger(ClientsSimulation.class);

    private static final int SENSORS = 4;

    /**
     * In this simulation each of the 4 sensors will send 30 measurements.
     * Each sensor can send 2 measurements per second.
     */
    public static void main(String[] args) {
        LOG.info("Initializing sensors");
        IntStream.range(1, SENSORS+1).boxed().map(i->new Sensor(String.valueOf(i))).forEach(Sensor::start);
    }
}
