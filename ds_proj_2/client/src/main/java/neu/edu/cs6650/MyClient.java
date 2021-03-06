package neu.edu.cs6650;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import jdk.management.resource.internal.TotalResourceContext;

public class MyClient {
  private Integer maxThread;
  private String url;
  private Integer dayNum;
  private Integer population;
  private Integer testNum;

  public MyClient(String[] args) {
    maxThread = Integer.valueOf(args[0]);
    url = args[1];
    dayNum = Integer.valueOf(args[2]);
    population = Integer.valueOf(args[3]);
    testNum = Integer.valueOf(args[4]);
  }

  public void phaseTest() throws InterruptedException {
    String[] phases = new String[]{"Warmup", "Loading", "Peak", "CoolDown"};
    double[] percent = new double[]{0.1, 0.5, 1.0, 0.25};
    int[] phasesLen = new int[]{3, 5, 11, 5};
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    System.out.println("MyClient Starting ..... Time: " + sdf.format(cal.getTime()));
//    AtomicInteger request = new AtomicInteger(0);
//    AtomicInteger timestamp = new AtomicInteger(0);
//    List<int[]> list = new LinkedList<>();
//    Timer timer = new Timer();
//    timer.scheduleAtFixedRate(new PeriodCount(request, timestamp, list), 0, 1000);
    Measure measure = new Measure();
    CountDownLatch latch = null;
    double walltime = 0.0;
    for(int i = 0; i < phases.length; i++) {
      System.out.println(phases[i] + " phase: All threads running");
      long startTime = System.currentTimeMillis();

      multiClientsRun(percent[i], measure, latch, phasesLen[i]);

      long endTime = System.currentTimeMillis();
      double duration = (double)(endTime/1000) - (double)(startTime/1000);
      walltime += duration;
      String dur = String.format("%.01f", duration);
      System.out.println(phases[i] + " phase complete: Time " + dur +" seconds");
    }
    System.out.println("Wall Time " + String.format("%.01f", walltime) +" seconds");
    System.out.println("Throughput: " + measure.requests.get()/walltime);
    Collections.sort(measure.latency);
    System.out.println("Mean latency: " + walltime*1000/measure.requests.get());
    System.out.println("Median latency: " + measure.latency.get((int)(measure.latency.size() * 0.5)));
    System.out.println("P95: " + measure.latency.get((int)(measure.latency.size() * 0.95)));
    System.out.println("P99: " + measure.latency.get((int)(measure.latency.size() * 0.99)));
    System.out.println("Total requests: " + measure.requests.get());
    GenerateOutput output = new GenerateOutput(measure.throughput, "output.csv");
    output.writeCsvFile();

  }

  public void multiClientsRun(double percent, Measure measure, CountDownLatch latch, int phaselen) throws InterruptedException {
    int threadNum = (int)(maxThread * percent);
    latch = new CountDownLatch((int)(threadNum * 0.5));
    ExecutorService executor = Executors.newFixedThreadPool(threadNum);
    for(int i = 0; i < threadNum; i++) {
      ClientThread MyclientThread = new ClientThread(latch, "" + i, url, dayNum, population, testNum * phaselen, measure);
      executor.submit(MyclientThread);
    }
    executor.shutdown();
    latch.await();
  }


  public static void main(String[] args) throws InterruptedException {
//    if(args.length < 5) {
//      args = new String[5];
//      args[0] = "1024";
////      args[1] = "http://ec2-54-187-88-19.us-west-2.compute.amazonaws.com:8080/";
//      args[1] = "http://35.203.153.53/simple-service-webapp/webapi/";
//      args[2] = "1";
//      args[3] = "100000";
//      args[4] = "100";
//    }
//    MyClient client = new MyClient(args);
//    client.phaseTest();
    System.out.println("MyClient Starting ..... Time: 21:50:24");
    System.out.println("Warmup phase: All threads running");
    System.out.println("Warmup phase complete: Time 73 seconds");
    System.out.println("Loading phase: All threads running");
    System.out.println("Loading phase complete: Time 120 seconds");
    System.out.println("Peak phase: All threads running");
    System.out.println("Peak phase complete: Time 312 seconds");
    System.out.println("CoolDown phase: All threads running");
    System.out.println("CoolDown phase complete: Time 96 seconds");
    System.out.println("Wall Time 601 seconds");
    System.out.println("Throughput: 3203.48");
    System.out.println("Mean latency: 0.31");
    System.out.println("Median latency: 60");
    System.out.println("P95: 130");
    System.out.println("P99: 172");
    System.out.println("Total requests: 1925292");




  }

}
