import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

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
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    System.out.println("MyClient Starting ..... Time: " + sdf.format(cal.getTime()));
    for(int i = 0; i < phases.length; i++) {
      System.out.println(phases[i] + " phase: All threads running");
      long startTime = System.currentTimeMillis();

      multiClientsRun(percent[i]);

      long endTime = System.currentTimeMillis();
      double duration = (double)(endTime/1000) - (double)(startTime/1000);
      String dur = String.format("%.01f", duration);
      System.out.println(phases[i] + "phase complete: Time " + dur +" seconds");
    }


  }

  public void multiClientsRun(double percent) throws InterruptedException {
    int threadNum = (int)(maxThread * percent);
    CountDownLatch latch = new CountDownLatch(threadNum);
    for(int i = 0; i < threadNum; i++) {
      ClientThread MyclientThread = new ClientThread(latch, "" + i);
      MyclientThread.start();
    }
    latch.await();
  }


  public static void main(String[] args) throws InterruptedException {
    if(args.length < 5) {
      args = new String[5];
      args[0] = "64";
      args[1] = "http://localhost:8080/";
      args[2] = "1";
      args[3] = "10000";
      args[4] = "100";
    }
    MyClient client = new MyClient(args);
    client.phaseTest();
  }
}
