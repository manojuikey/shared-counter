package com.cluster.counter.zk;

import java.io.File;

import org.apache.curator.test.InstanceSpec;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;
import org.junit.*;

import com.cluster.counter.*;
import com.cluster.counter.zk.ZookeeperBaseCounterFactory;

/**
 * Test to validate Counter value is persisted across cluster reboot.
 */
public class CounterFactoryPersistenceTest {

	private static TestingServer testServer;

	@Test
	public void test_counter_persisted() throws Exception {
		InstanceSpec spec1 = new InstanceSpec(new File("target/test-classes/zookeeper1"), 3181, 2888, 3888, false, 1);
		testServer = new TestingServer(spec1, true);

		CounterFactory factory = new ZookeeperBaseCounterFactory(testServer.getConnectString());

		Counter counter = factory.getCounter("testCounter");
		int preRebootValue = counter.incrementAndGet();

		// Reboot the server
		CloseableUtils.closeQuietly(testServer);
		testServer = new TestingServer(spec1, true);
		factory = new ZookeeperBaseCounterFactory(testServer.getConnectString());

		counter = factory.getCounter("testCounter");
		Assert.assertEquals(preRebootValue, counter.getCurrentValue());
	}

	@AfterClass
	public static void clean() {
		CloseableUtils.closeQuietly(testServer);
	}
}
