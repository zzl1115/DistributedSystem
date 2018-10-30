package neu.edu.cs6650;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Measure {
  List<Long> latency;
  Map<Integer, Integer> throughput;
  Long globalStartTime;
  AtomicLong requests;

  public Measure() {
    latency = Collections.synchronizedList(new LinkedList<Long>());
    throughput = new ConcurrentHashMap<>();
    globalStartTime = System.currentTimeMillis();
    requests = new AtomicLong(0);
  }
}
