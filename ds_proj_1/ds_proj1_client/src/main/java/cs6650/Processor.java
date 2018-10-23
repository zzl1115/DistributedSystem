package cs6650;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Processor implements Runnable {
  private int iterationNum;
  public MyClient myClient;
  public int count;
  public int request;
  public List<Long> latency;

  public Processor(int iterationNum, MyClient myClient) {
    this.myClient = myClient;
    this.iterationNum = iterationNum;
    count = 0;
    request = 0;
    latency = new ArrayList<>();
  }
  @Override
  public void run() {
    for(int i = 0; i < iterationNum; i++) {
      long startTime = System.currentTimeMillis();
//      try {
//        myClient.get();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }

      myClient.postText();
      myClient.postText();
      long endTime = System.currentTimeMillis();
      latency.add(endTime - startTime);
      request++;
    }
    count = myClient.getCount();
  }
}
