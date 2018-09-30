package cs6650;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Main class.
 *
 */
public class Main {
    // Base URI the Grizzly HTTP server will listen on
    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
      args = new String[]{"100", "100", "http://localhost:8080/webapi/myresource", "8080"};
      int maxThread = Integer.valueOf(args[0]);
      int iterationNum = Integer.valueOf(args[1]);
      String ip = args[2];
      int port = Integer.valueOf(args[3]);
      Client client = ClientBuilder.newClient();
      WebTarget webTarget = client.target(args[2]);
      double[] phases = new double[]{0.1, 0.5, 1.0, 0.25};
      String[] strs = new String[]{"Warmup", "Loading", "Peak", "Cooldown"};
      double total = 0;
      int request = 0;
      int succ = 0;
      List<Long> list = new ArrayList<>();
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
      System.out.println("Client Starting ..... Time: " + sdf.format(cal.getTime()));
      for(int i = 0; i < phases.length; i++) {
        int pool = (int)(maxThread * phases[i]);
      //  System.out.println(pool);
        ExecutorService executor = Executors.newFixedThreadPool(pool);
        int curReq = 0;
        int curSuc = 0;
        List<Processor> processors = new ArrayList<>();
        for(int j = 0; j < pool; j++) {
          MyClient myClient = new MyClient(webTarget);
          Processor processor = new Processor(iterationNum, myClient);
          processors.add(processor);
          executor.submit(processor);
        }
        System.out.println(strs[i] + " phase: All threads running");
        long startTime = System.currentTimeMillis();
        executor.shutdown();
        try {
          executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        for(Processor processor : processors){
          curReq += processor.myClient.getRequest();
          curSuc += processor.myClient.getCount();
          list.addAll(processor.latency);
        }
        long endTime = System.currentTimeMillis();
        double duration = (double)(endTime - startTime)/1000;
        total += duration;
        String dur = String.format("%.01f", duration);
        System.out.println(strs[i] + "phase complete: Time " + dur +" seconds");
   //     System.out.println(curReq + "---" + curSuc);
        request += curReq;
        succ += curSuc;
      }
      Collections.sort(list);
      long sum = 0;
      for(long e : list) sum += e;
      long mean = sum / list.size();
      System.out.println("===============================================");
      System.out.println("Total number of requests sent: " + request);
      System.out.println("Total number of Successful responses: " + succ);
      String toltalDur = String.format("%.01f", (double)total);
      System.out.println("Test Wall Time: "+ toltalDur + " seconds");
      System.out.println("Overall throughput across all phases: " + String.format("%.01f",(double)request/total));
      System.out.println("Mean latency: " + mean + " ms");
      System.out.println("Median latency: " + list.get(list.size()/2) + " ms");
      System.out.println("99th percentile latency: " + list.get(list.size() * 99 / 100) + " ms");
      System.out.println("95th percentile latency: " + list.get(list.size() * 95 / 100) + " ms");
      client.close();

    }
}

