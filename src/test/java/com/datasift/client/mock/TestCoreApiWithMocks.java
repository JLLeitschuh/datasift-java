package com.datasift.client.mock;

import com.datasift.client.IntegrationTestBase;
import com.datasift.client.core.Balance;
import com.datasift.client.core.Dpu;
import com.datasift.client.core.Stream;
import com.datasift.client.core.Usage;
import com.datasift.client.core.Validation;
import com.datasift.client.mock.datasift.MockCoreApi;
import io.higgs.core.HiggsServer;
import io.higgs.core.ObjectFactory;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestCoreApiWithMocks extends IntegrationTestBase {
    private HiggsServer server;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> streams = new HashMap<>();
    private MockCoreApi m = new MockCoreApi();
    private String csdl = "";
    private String hash = "";
    private float dpu = 0;
    private DateTime createdAt = DateTime.now(), start = DateTime.now().plusHours(1), end = DateTime.now().plusDays(1);
    private int secs = new Random().nextInt();
    private Map<String, Object> detail = new HashMap<>();
    private int count = new Random().nextInt();
    private double credit = -1f;
    private String plan = "";
    private double remaining_dpus = -1f;


    @Before
    public void setup() throws IOException, IllegalAccessException, Exception {
        server = MockServer.startNewServer();
        config.host("localhost");
        config.setSslEnabled(false);
        config.port(server.getConfig().port);
        super.setup();
        headers.put("server", "nginx/0.8.55");
        headers.put("x-ratelimit-limit", "10000");
        headers.put("x-ratelimit-remaining", "10000");
        headers.put("x-ratelimit-cost", "5");
        server.registerObjectFactory(new ObjectFactory(server) {
            public Object newInstance(Class<?> aClass) {
                m.setHeaders(headers);
                return m;
            }

            public boolean canCreateInstanceOf(Class<?> aClass) {
                return MockCoreApi.class.isAssignableFrom(aClass);
            }
        });

        SecureRandom random = new SecureRandom();
        csdl = "interaction.content contains \"apple\"";
        hash = new BigInteger(130, random).toString(32);
        dpu = Float.valueOf(String.valueOf(Math.random()));
        createdAt = DateTime.now();
        streams.put("seconds", secs);
        streams.put("licenses", new HashMap<String, Integer>());


        m.setExpectedCsdl(csdl);
        m.setDpu(dpu);
        m.setCreatedAt(createdAt);
        m.setCompileHash(hash);
        m.setExpectedCsdl(csdl);
        m.setStart(start);
        m.setEnd(end);

        m.setCredit(credit);
        m.setPlan(plan);
        m.setRemaining_dpus(remaining_dpus);


        Usage.UsageStream usageStream = new Usage.UsageStream();
        usageStream.setLicenses(new HashMap<String, Integer>());
        usageStream.setSeconds(secs);

        m.setStreams(usageStream);

        //TODO complete the dpu test & DpuDetails class
//        Map<String, Object> detailData = new HashMap<>();
//        Map<String, Object> containsData = new HashMap<>();
//        containsData.put("count", count);
//        containsData.put("dpu", dpu);
//        containsData.put("targets", )

//        detailData.put("contains", containsData);
//        detail.put("detail", detailData);
    }

    @Test
    public void testIfUserCanValidateCSDL() {
        Validation validation = datasift.validate(csdl).sync();
        assertTrue(validation.isSuccessful());

        DateTime actualDate = validation.getCreatedAt();
        float actualDpu = validation.getDpu();

        assertEquals(createdAt.getMillis(), actualDate.getMillis());
        assertEquals(dpu, actualDpu, 0.00000001);
    }

    @Test
    public void testIfUserCanCompile() {
        Stream stream = datasift.compile(csdl).sync();
        assertTrue(stream.isSuccessful());

        DateTime actualDate = stream.getCreatedAt();
        float actualDpu = stream.getDpu();

        assertEquals(createdAt.getMillis(), actualDate.getMillis());
        assertEquals(dpu, actualDpu, 0.00000001);

        assertEquals(hash, stream.hash());
    }

    @Test
    public void testIfObjectsProcessedAndDelivered() {

        Usage usage = datasift.usage().sync();

        assertTrue(usage.isSuccessful());

        assertEquals(usage.getEnd().getMillis(), end.getMillis());
        assertEquals(usage.getStart().getMillis(), start.getMillis());

        assertEquals(usage.getStreams().getSeconds(), streams.get("seconds"));

        for (String key : usage.getStreams().getLicenses().keySet()) {
            if (!streams.containsKey(key)) {
                throw new AssertionError("The licenses map keys are not the same");
            }

            if (!streams.get(key).equals(usage.getStreams().getLicenses().get(key))) {
                throw new AssertionError("The licenses map values are not the same");
            }
        }
    }

    @Test
    public void testIfUserCanCalculateDpuCost() {

        Stream stream = Stream.fromString("13e9347e7da32f19fcdb08e297019d2e");
        Dpu dpu = datasift.dpu(stream).sync();

        assertTrue(dpu.isSuccessful());

        assertEquals(dpu.getDpu(), this.dpu, 0.00000001);


    }

    @Test
    public void testIfUserCanCalculateBalance() {
        Balance balance = datasift.balance().sync();
        assertTrue(balance.isSuccessful());

        assertEquals(balance.credit(), credit, 0.00000001);
        assertEquals(balance.pricePlan(), plan);
        assertEquals(balance.remainingDpus(), remaining_dpus, 0.00000001);

    }

    @After
    public void after() {
        server.stop();
    }
}