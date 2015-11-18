package com.cluster.counter.zk;

import java.io.File;
import java.util.concurrent.*;

import org.apache.curator.test.*;
import org.apache.curator.utils.CloseableUtils;
import org.junit.*;

import com.cluster.counter.*;
import com.cluster.counter.zk.ZookeeperBaseCounterFactory;

/**
 * Test the CounterFactory with 3 Zookeeper server ensemble.
 */
public class CounterFactoryQuoramTest {

	private static TestingCluster testCluster;
	private static final int POOL_SIZE = 5;

	@BeforeClass
	public static void setup() throws Exception {
		InstanceSpec spec1 = new InstanceSpec(new File("target/test-classes/zookeeper1"), 2181, 2888, 3888, false, 1);
		InstanceSpec spec2 = new InstanceSpec(new File("target/test-classes/zookeeper2"), 2281, 4888, 5888, false, 2);
		InstanceSpec spec3 = new InstanceSpec(new File("target/test-classes/zookeeper3"), 2381, 6888, 7888, false, 3);
		testCluster = new TestingCluster(spec1, spec2, spec3);
		testCluster.start();
	}

	@Test
	public void testCluster() throws Exception {
		final ExecutorService service = Executors.newFixedThreadPool(POOL_SIZE);
		final CounterFactory factory = new ZookeeperBaseCounterFactory(testCluster.getConnectString());
		final Counter counter = factory.getCounter("someCounter");
		counter.reset();
		Assert.assertEquals(0, counter.getCurrentValue());

		for (int index = 0; index < POOL_SIZE; index++) {
			Runnable task = new Runnable() {
				@Override
				public void run() {
					final Counter innerCounter = factory.getCounter("someCounter");
					innerCounter.incrementAndGet();
					innerCounter.decrementAndGet();
				}
			};
			service.submit(task);
		}
		service.shutdown();
		Assert.assertTrue(service.awaitTermination(60, TimeUnit.SECONDS));
		Assert.assertEquals(0, counter.getCurrentValue());
	}

	@AfterClass
	public static void clean() {
		CloseableUtils.closeQuietly(testCluster);
	}
}
