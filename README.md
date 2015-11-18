# Distributed Atomic Counter

Distributed Counter is a thread safe clustered counter that can be concurrently accessed by multiple clients to perform increment or decrement operation.
It  uses Zookeeper Ensemble to ensure the cluster is well co-ordinated and utilizes the open source Curator Frmaework built to ease Zookeeper handling. Curator frmaework is developed by Netflix and open-sourced to Apache foundation.

![Alt text](images/arch.jpg?raw=true "Architecture")

The Server listens on a TCP port which can be specified while starting it, it expects the input requests in JSON format and uses Google's GSON library to process the input.

The Client java program takes input as program argument server's address, port and counter name and operation type. 

----
## com.cluster.counter.Counter
Thread safe clustered counter which provides atomic operations.
  - incrementAndGet()
  -- Increments and returns the new value of the counter
  - decrementAndGet()
   -- Decrement and returns the new value
  - getCurrentValue()
  -- Return current value of the counter
  - reset()
   -- Resets the counter value to 0
----

## com.cluster.counter.CounterFactory
Counter factory
- getCounter(countrtName) - Returns the Counter by it's name
---

## com.cluster.counter.server.ServerMain
Server to listen for clustered counter request from multiple clients
Highely scalable as it uses Zookeeper Ensemble so many server can be started in a cluster to serve clients.

    usage : 
    
    com.cluster.counter.server.ServerMain <PORT> <[hostname1:port1...]>
    
    Where PORT - where server will listen for client request.
    Zookeeper Enseble Address - Comma separated server address where zookeeper quoram is running i.e. [hostname1:port1,hostname2:port2....] 

---

## com.cluster.counter.client.ClientMain
Client to send counter request to a running server

    usage : 
    
    com.cluster.counter.client.ClientMain <ServerName> <Server Port> <CounterName> <INCREMENT_AND_GET|DECREMENT_AND_GET>
    
    Where Server Host and port - where server is listening.

---
## How To test/build

$ mvn clean install

## How To Run Servers and Clients 
   
   The Maven build uses Embeded Zookeeper server using Apache Curator utility, But whole system can be connected to existing/extenral Zookeeper Ensemble.
   
   The Zookeeper distribution can be downloaded from apache mirror.
   
### Start zookeeper

Download the zookeeper distribution from this link

	https://www.apache.org/dist/zookeeper/zookeeper-3.4.6/

	Unzip the file
$ tar -xvzf zookeeper-3.4.6.tar.gz

	Create a zoo.cfg file with this content in zookeeper-3.4.6/conf/zoo.conf location
	
	--
	tickTime=2000
	dataDir=/tmp/zookeeper
	clientPort=2181
	--
	

$ cd zookeeper-3.4.6/bin/

$ ./zkServer.sh  start   # this will start zookeeper with default 2181 port, and uses data directory /tmp/zookeeper

$ ./zkServer.sh  stop   # this will stop zookeeper with default 2181 port

### Start the Server
many server can be started on different host, port, specify the zookeeper connect address (0.0.0.0:2181 here)

$  mvn exec:java -Dexec.mainClass=com.cluster.counter.server.ServerMain -Dexec.args="4445 0.0.0.0:2181"

### Now run the client
start the client and connect to any one of the server started in the previous step.

$ mvn exec:java -Dexec.mainClass=com.cluster.counter.client.ClientMain -Dexec.args="localhost 4445 mycounter INCREMENT\_AND\_GET"


### How To generate javadoc, PMD, code coverage etc

$ mvn clean site
