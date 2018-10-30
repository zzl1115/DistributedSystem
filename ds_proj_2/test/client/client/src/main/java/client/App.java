package client;

import java.io.FileWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.googlecode.jcsv.writer.CSVWriter;
import com.sun.jersey.api.client.Client;

import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class App
{
    String URL = new String(); // URL for AWS EC2
    String localURL = "localhost:8080/";
    int threads = 1;
    int numOfTest = 1;
    int population = 100000;
    int days = 1;
    long[] result = new long[8];
    double overLappingRatio = 0.5;
    Metrics metrics;

    private void multiRequest(int numOfThreads, int[] timeInterval, CountDownLatch latch) {
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        for(int i = 0; i < numOfThreads; i++) {
            executor.submit(new Runner(URL, numOfTest, population, days, timeInterval, latch, metrics));
        }
        executor.shutdown();
        //executor.awaitTermination(60, TimeUnit.DAYS);
    }

    public void mesureLatency(){
        Collections.sort(metrics.latency);
        Long total = Long.valueOf(0);
        for(Long data : metrics.latency) total += data;
        int size = metrics.latency.size();
        if(size == 0) return;
        result[2] = metrics.successfulRequest.get();
        result[4] = total / size; // mean
        result[5] = metrics.latency.get(size / 2); // median
        result[6] = metrics.latency.get((int) Math.round(size * 0.95) - 1); // 95th
        result[7] = metrics.latency.get((int) Math.round(size * 0.99) - 1); // 99th
        printResult();
    }

    public void printResult(){
      System.out.println("Total run time (wall time) is : " + result[0]);
      System.out.println("Total Number of requests sent : " + result[1]);
      System.out.println("Total Number of successful requests : " + result[2]);
      System.out.println("=================================================");
      System.out.println("Measurement is as below: ");
      System.out.println("Total run time (wall time) is : " + result[0]);
      System.out.println("Total Number of requests sent : " + result[1]);
      System.out.println("Total Number of successful requests : " + result[2]);
      System.out.println("Overall throughput is : " + result[3]);
      System.out.println("Mean latency is : " + result[4]);
      System.out.println("Median latency is : " + result[5]);
      System.out.println("95th latency is : " + result[6]);
      System.out.println("99th latency is : " + result[7]);
    }

    public void startLoad(int threads) throws InterruptedException {
        String[] phases = new String[]{"Warmup", "Loading", "Peak", "CoolDown"};
        int[][] intervals = new int[][]{new int[]{0,2}, new int[]{3,7},new int[]{8,18},new int[]{19,23}};
        double[] percent = new double[]{0.1, 0.5, 1.0, 0.25};
        int phase0Threads = (int)(threads * percent[0]);
        int phase1Threads = (int)(threads * percent[1]);
        int phase2Threads = (int)(threads * percent[2]);
        int phase3Threads = (int)(threads * percent[3]);
        //Warmup
        long totalStartTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch((int)(phase0Threads * overLappingRatio));
        System.out.println("Phase 0 started with countdown as " + (int)(phase0Threads * overLappingRatio));
        startPhase(phases[0], phase0Threads, intervals[0], latch);
        latch.await();
        System.out.println("Phase 0 finished");

        //Loading
        latch = new CountDownLatch((int)(phase1Threads * overLappingRatio));
        System.out.println("Phase 1 started with countdown as " + (int)(phase1Threads * overLappingRatio));
        startPhase(phases[1], phase1Threads, intervals[1], latch);
        latch.await();
        System.out.println("Phase 1 finished");

        //Peaking
        latch = new CountDownLatch((int)(phase2Threads * overLappingRatio));
        System.out.println("Phase 2 started with countdown as " + (int)(phase2Threads * overLappingRatio));
        startPhase(phases[2], phase2Threads, intervals[2], latch);
        latch.await();
        System.out.println("Phase 2 finished");

        //Cooling down
        latch = new CountDownLatch(phase3Threads);
        System.out.println("Phase 3 started with countdown as " + phase3Threads);
        startPhase(phases[3], phase3Threads, intervals[3], latch);
        latch.await();
        System.out.println("Phase 3 finished");
        long totalEndTime = System.currentTimeMillis();
        result[0] = totalEndTime - totalStartTime;
        result[1] = (long) (threads * numOfTest * 24 * (0.1 + 0.5 + 1 + 0.25) * 2); // wall time
        result[3] = result[1] / (result[0] / 1000);//throughput
    }

    public void startPhase(String phase, int numOfthreads, int[] interval, CountDownLatch latch)
            throws InterruptedException {
        System.out.println(phase + " phase: All " + numOfthreads + " threads running ….");
        long startTime = System.currentTimeMillis();
        multiRequest(numOfthreads, interval, latch);
        long endTime = System.currentTimeMillis();
        System.out.println(phase + " phase complete: Time: " + (endTime - startTime) + "ms");
    }

    public void preprocess(String[] args, App app){
        app.threads = Integer.valueOf(args[0]);
        app.days = Integer.valueOf(args[2]);
        app.population = Integer.valueOf(args[3]);
        app.numOfTest = Integer.valueOf(args[4]);
        String ip = app.getIp(args[1]);
        String port = "8080";
        app.URL = "http://ec2-54-212-45-215.us-west-2.compute.amazonaws.com:" + port + "/"; //http://ec2-" + ip + ".compute-1.amazonaws.com:" + port + "/";
        //http://ec2-54-212-45-215.us-west-2.compute.amazonaws.com:8080
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.out.println("Client starting ….. Time: " + sdf.format(cal.getTime()));
    }

    public String getIp(String ip){
        String[] arr = ip.split("\\.");
        ip = new String();
        for(String str : arr){
            ip = ip + "-" + str;
        }
        ip = ip.substring(1);
        return ip;
    }

    public static void main( String[] args) throws Exception {
        App app = new App();
        //35.173.193.106
        args = new String[]{"10", "54.212.45.215", "365", "100000", "2"};
        app.metrics = new Metrics();
        app.preprocess(args, app); // handle the info of args
        app.startLoad(app.threads); // start 4 phases
        app.mesureLatency();
        try (ICsvListWriter listWriter = new CsvListWriter(new FileWriter("/Users/stoneruirui/Desktop/cs/CS6650-Distribution-System/HW2/DSHW2/data_thread128.csv"),
                CsvPreference.STANDARD_PREFERENCE)){
            for (Map.Entry<Integer, Integer> entry : app.metrics.graph.entrySet()){
                listWriter.write(entry.getKey(), entry.getValue());
            }
        }
    }
}
