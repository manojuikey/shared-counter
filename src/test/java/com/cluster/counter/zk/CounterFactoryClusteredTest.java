package com.cluster.counter.zk;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.curator.test.InstanceSpec;
import org.apache.curator.test.TestingCluster;
import org.apache.curator.utils.CloseableUtils;
import org.junit.*;

import com.cluster.counter.*;
import com.cluster.counter.client.ClusteredCounterClient;
import com.cluster.counter.server.ClusteredCounterServer;
import com.cluster.counter.zk.ZookeeperBaseCounterFactory;

public class CounterFactoryClusteredTest {

	private static TestingCluster testCluster;
	private static ClusteredCounterServer.Signal signal1 = new ClusteredCounterServer.Signal();
	private static ClusteredCounterServer.Signal signal2 = new ClusteredCounterServer.Signal();	

	@BeforeClass
	public static void setup() throws Exception {
		InstanceSpec spec1 = new InstanceSpec(new File("target/test-classes/zookeeper1"), 2181, 2888, 3888, false, 1);
		InstanceSpec spec2 = new InstanceSpec(new File("target/test-classes/zookeeper2"), 2281, 4888, 5888, false, 2);
		InstanceSpec spec3 = new InstanceSpec(new File("target/test-classes/zookeeper3"), 2381, 6888, 7888, false, 3);
		testCluster = new TestingCluster(spec1, spec2, spec3);
		testCluster.start();
		
		final CountDownLatch latch = new CountDownLatch(1);
		/*
		 * Start TCP server 1
		 */
		new Thread() {
			public void run() {try {
				ClusteredCounterServer.startServer(7111, testCluster.getConnectString(), signal1);
			} catch (IOException e) {
				e.printStackTrace();
			}};
		}.start();
		
		
		/*
		 * Start TCP server 2
		 */
		new Thread() {
			public void run() {try {
				ClusteredCounterServer.startServer(7222, testCluster.getConnectString(), signal2);
			} catch (IOException e) {
				e.printStackTrace();
			}};
		}.start();
		
		latch.await(5, TimeUnit.SECONDS);
	}

	@Test
	public void testMultipleCluster() throws Exception {
		CounterFactory factory = new ZookeeperBaseCounterFactory(testCluster.getConnectString());
		final Counter counter = factory.getCounter("distcounter");
		counter.reset();
		int preValue = factory.getCounter("distcounter").getCurrentValue();
		
		ClusteredCounterClient.sendRequest("localhost", 7111, "distcounter", OpType.INCREMENT_AND_GET.name());
		ClusteredCounterClient.sendRequest("localhost", 7222, "distcounter", OpType.INCREMENT_AND_GET.name());
		ClusteredCounterClient.sendRequest("localhost", 7111, "distcounter", OpType.DECREMENT_AND_GET.name());
		
		Assert.assertEquals(preValue + 1, counter.getCurrentValue());
	}

	@AfterClass
	public static void clean() throws Exception, IOException {
		signal1.stop();
		signal2.stop();
		ClusteredCounterClient.sendRequest("localhost", 7222, "dummysignal", OpType.INCREMENT_AND_GET.name());
		ClusteredCounterClient.sendRequest("localhost", 7111, "dummysignal", OpType.DECREMENT_AND_GET.name());
		CloseableUtils.closeQuietly(testCluster);
	}
}
