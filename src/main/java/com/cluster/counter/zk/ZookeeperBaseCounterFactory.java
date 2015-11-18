
package com.cluster.counter.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.cluster.counter.*;

public class ZookeeperBaseCounterFactory implements CounterFactory {

	private final CuratorFramework client;
	public ZookeeperBaseCounterFactory(final String connectionString) {
		client = CuratorFrameworkFactory.newClient(connectionString,
				new ExponentialBackoffRetry(1000, 3));
		client.start();
	}
	@Override
	public Counter getCounter(final String counterName) {
		Counter counter = null;
		try {
			counter =  DistributedCounterManager.getInstance().createOrGetCounter(client, counterName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return counter;
	}
}
