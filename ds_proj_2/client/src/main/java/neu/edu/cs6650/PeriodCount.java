package neu.edu.cs6650;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PeriodCount extends TimerTask {
  AtomicInteger requests;
  List<int[]> list = new ArrayList<>();
  AtomicInteger timestamp;

  public PeriodCount(AtomicInteger requests, AtomicInteger timestamp, List<int[]> list) {
    this.requests = requests;
    this.timestamp = timestamp;
    this.list = list;
  }

  @Override
  public void run() {
    int[] pair = new int[]{timestamp.intValue(), requests.intValue()};
    list.add(pair);
    requests.set(0);
    timestamp.getAndIncrement();
  }
}
