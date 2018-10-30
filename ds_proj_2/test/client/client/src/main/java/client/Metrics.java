package client;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Metrics {
  List<Long> latency;
  ConcurrentHashMap<Integer, Integer> graph;
  Long globalStartTime;
  AtomicLong successfulRequest;

  public Metrics(){
    this.latency = Collections.synchronizedList(new LinkedList<Long>());
    this.graph = new ConcurrentHashMap<>();
    globalStartTime = System.currentTimeMillis();
    successfulRequest = new AtomicLong();
  }
}
