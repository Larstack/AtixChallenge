package com.atixlabs.server.task;

import com.atixlabs.message.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import static com.atixlabs.utils.Utils.round;

/**
 * @author larstack
 */
public class MeasurementsProcessor implements Runnable {

    private static final Logger LOG = LogManager.getLogger(MeasurementsProcessor.class);

    private BlockingQueue<Message> queue;
    private BigDecimal S;
    private BigDecimal M;

    public MeasurementsProcessor(BlockingQueue<Message> queue, BigDecimal s, BigDecimal m) {
        this.queue = queue;
        this.S = s;
        this.M = m;
    }

    @Override
    public void run() {
        LOG.info("Processing measurements");
        List<Message> messages = new ArrayList<>();
        queue.drainTo(messages);
        LOG.debug("Messages to process: " + messages);
        DoubleSummaryStatistics summary = messages.stream()
                .map(Message::getMeasurement).map(BigDecimal::doubleValue).collect(Collectors.summarizingDouble(Double::doubleValue));
        BigDecimal average = round(summary.getAverage());
        LOG.debug("Average: " + average);
        BigDecimal max = round(summary.getMax());
        LOG.debug("Max value: " + max);
        BigDecimal min = round(summary.getMin());
        LOG.debug("Min value: " + min);

        if(validateDifferenceBetweenMinAndMax(max, min)){
            LOG.error("The difference between min and max is greater than " + S);
        }

        if(validateAverage(average)){
            LOG.error("The average is greater than " + M);
        }

        LOG.info("Process finished");
    }

    private boolean validateAverage(BigDecimal average) {
        return average.compareTo(M) > 0;
    }

    private boolean validateDifferenceBetweenMinAndMax(BigDecimal max, BigDecimal min) {
        BigDecimal difference = min.subtract(max);
        LOG.debug("Difference between " + min + " and " + max + " is: " + difference);
        return difference.compareTo(S) > 0;
    }
}
