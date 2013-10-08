package com.datasift.client;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class FutureDataTest {
    @Test(expected = IllegalArgumentException.class)
    public void testWrap() throws Exception {
        FutureData.wrap(null);
    }

    @Test
    public void testReceived() throws Exception {
        FutureData<DataSiftResult> res = new FutureData<DataSiftResult>();
        assertNull("Data can't be present before received is invoked", res.data);
        DataSiftResult result = new DataSiftResult();
        res.received(result);
        assertNotNull("Data shouldn't be null after received is invoked", res.data);
        assertSame("The same data object should be presented", result, res.data);
    }

    @Test(expected = CallbackEx.class)
    public void testOnData() throws Exception {
        FutureData<DataSiftResult> res = new FutureData<DataSiftResult>();
        res.onData(new FutureResponse<DataSiftResult>() {
            public void apply(DataSiftResult data) {
                assertNotNull(data);
                throw new CallbackEx(); //throw to ensure this is invoked
            }
        });
        res.received(new DataSiftResult());
    }

    public static class CallbackEx extends RuntimeException {
    }
}