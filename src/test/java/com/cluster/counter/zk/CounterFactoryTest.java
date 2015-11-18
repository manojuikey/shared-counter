package com.cluster.counter.zk;

import java.io.File;

import org.apache.curator.test.InstanceSpec;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;
import org.junit.*;

import com.cluster.counter.*;
import com.cluster.counter.zk.ZookeeperBaseCounterFactory;

public class CounterFactoryTest {

	private static TestingServer testServer;

	@BeforeClass
	public static void setup() throws Exception {
		InstanceSpec spec1 = new InstanceSpec(new File("target/test-classes/zookeeper1"), 3181, 2888, 3888, false, 1);
		testServer = new TestingServer(spec1, true);
	}

	@Test
	public void test_increment() throws Exception {
		CounterFactory factory = new ZookeeperBaseCounterFactory(testServer.getConnectString());

		final Counter counter = factory.getCounter("testCounter");
		final int current = counter.getCurrentValue();
		int newValue = counter.incrementAndGet();
		Assert.assertEquals(current + 1, newValue);
	}

	@Test
	public void test_decrement() throws Exception {
		CounterFactory factory = new ZookeeperBaseCounterFactory(testServer.getConnectString());

		final Counter counter = factory.getCounter("testCounter");
		final int current = counter.getCurrentValue();

		int newValue = counter.decrementAndGet();
		Assert.assertEquals(current - 1, newValue);
	}

	@Test
	public void test_reset() throws Exception {
		CounterFactory factory = new ZookeeperBaseCounterFactory(testServer.getConnectString());
		final Counter counter = factory.getCounter("testCounter");
		counter.incrementAndGet();
		counter.reset();
		Assert.assertEquals(0, counter.getCurrentValue());
	}

	@AfterClass
	public static void clean() {
		CloseableUtils.closeQuietly(testServer);
	}
}
