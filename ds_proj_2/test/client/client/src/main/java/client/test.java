package client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public class test
{
  public static void main(String args[])
          throws InterruptedException
  {
    for(int i = 0; i < 100; i++){
      System.out.println(ThreadLocalRandom.current().nextInt(1000));
    }
    // Let us create task that is going to
    // wait for four threads before it starts
    CountDownLatch latch = new CountDownLatch(0);

    // Let us create four worker
    // threads and start them.
    Worker first = new Worker(1000, latch,
            "WORKER-1");
    Worker second = new Worker(2000, latch,
            "WORKER-2");
    Worker third = new Worker(5000, latch,
            "WORKER-3");

//    first.start();
//    second.start();
//    third.start();
//    // The main task waits for four threads
//    latch.await();
//
//    //CountDownLatch latch2 = new CountDownLatch(1);
//    latch = new CountDownLatch(2);
//    Worker fourth = new Worker(1000, latch,
//            "WORKER-4");
//    Worker fifth = new Worker(1000, latch, "WORKER-5");
//    fourth.start();
//    fifth.start();
//    latch.await();
//
//    latch = new CountDownLatch(1);
//    Worker sixth = new Worker(100, latch, "WORKER-6");
//    sixth.start();
//    latch.await();
//    // Main thread has started
//    System.out.println("main1" +
//            " has finished");
  }
}

// A class to represent threads for which 
// the main thread waits. 
class Worker extends Thread
{
  private int delay;
  private CountDownLatch latch;

  public Worker(int delay, CountDownLatch latch,
                String name)
  {
    super(name);
    this.delay = delay;
    this.latch = latch;
  }

  @Override
  public void run()
  {
    try
    {
      Thread.sleep(delay);
      latch.countDown();
      System.out.println(Thread.currentThread().getName()
              + " finished");
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
} 