package com.atixlabs;

import com.atixlabs.message.Message;
import com.atixlabs.server.task.MeasurementsProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import static com.atixlabs.utils.Utils.round;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

/**
 * @author larstack
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MeasurementsProcessor.class)
public class MonitorServerTests {

    private static final String AVERAGE_METHOD =  "validateAverage";
    private static final String DIFFERENCE_METHOD =  "validateDifferenceBetweenMinAndMax";

    @Captor
    private ArgumentCaptor<BigDecimal> firstCaptor;
    @Captor
    private ArgumentCaptor<BigDecimal> secondCaptor;

    @Test
    public void processorFlowTest() throws Exception {
        ArrayBlockingQueue queue = new ArrayBlockingQueue(5, true);
        queue.put(createMessage(BigDecimal.valueOf(3)));
        queue.put(createMessage(BigDecimal.valueOf(1)));
        queue.put(createMessage(BigDecimal.valueOf(30.4)));
        queue.put(createMessage(BigDecimal.valueOf(-5)));
        queue.put(createMessage(BigDecimal.valueOf(1.43)));

        BigDecimal s = BigDecimal.valueOf(-50);
        BigDecimal m = BigDecimal.valueOf(4);

        MeasurementsProcessor processor = spy(new MeasurementsProcessor(queue, s, m));

        processor.run();

        verifyPrivate(processor).invoke(DIFFERENCE_METHOD, firstCaptor.capture(), firstCaptor.capture());
        List<BigDecimal> values = firstCaptor.getAllValues();
        assertEquals(round(30.40), values.get(0));
        assertEquals(round(-5d), values.get(1));
        assertEquals(0, queue.size());

        verifyPrivate(processor).invoke(AVERAGE_METHOD, secondCaptor.capture());
        assertEquals(round(6.166), secondCaptor.getValue());
    }

    @Test
    public void differenceBetweenMinAndMaxIsGreaterThanS() throws Exception {
        BigDecimal s = BigDecimal.valueOf(-50);
        BigDecimal m = BigDecimal.valueOf(4);
        ArrayBlockingQueue queue = new ArrayBlockingQueue(5, true);
        MeasurementsProcessor processor = spy(new MeasurementsProcessor(queue, s, m));

        assertTrue(Whitebox.invokeMethod(processor, DIFFERENCE_METHOD, BigDecimal.valueOf(30.4), BigDecimal.valueOf(-5)));
    }

    @Test
    public void differenceBetweenMinAndMaxIsLessThanS() throws Exception {
        BigDecimal s = BigDecimal.valueOf(0);
        BigDecimal m = BigDecimal.valueOf(4);
        ArrayBlockingQueue queue = new ArrayBlockingQueue(5, true);
        MeasurementsProcessor processor = spy(new MeasurementsProcessor(queue, s, m));

        assertFalse(Whitebox.invokeMethod(processor, DIFFERENCE_METHOD, BigDecimal.valueOf(30.4), BigDecimal.valueOf(-5)));
    }

    ////////////

    @Test
    public void averageIsGreaterThanM() throws Exception {
        BigDecimal s = BigDecimal.valueOf(-50);
        BigDecimal m = BigDecimal.valueOf(4);
        ArrayBlockingQueue queue = new ArrayBlockingQueue(5, true);
        MeasurementsProcessor processor = spy(new MeasurementsProcessor(queue, s, m));

        assertTrue(Whitebox.invokeMethod(processor, AVERAGE_METHOD, BigDecimal.valueOf(4.1)));
    }

    @Test
    public void averageIsLessThanM() throws Exception {
        BigDecimal s = BigDecimal.valueOf(-50);
        BigDecimal m = BigDecimal.valueOf(4);
        ArrayBlockingQueue queue = new ArrayBlockingQueue(5, true);
        MeasurementsProcessor processor = spy(new MeasurementsProcessor(queue, s, m));

        assertFalse(Whitebox.invokeMethod(processor, AVERAGE_METHOD, BigDecimal.valueOf(3.9)));
    }

    private Message createMessage(BigDecimal value) {
        return new Message(value);
    }
}
