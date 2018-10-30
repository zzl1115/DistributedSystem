package client;

import com.sun.jersey.api.client.WebResource;


import org.glassfish.jersey.client.ClientConfig;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Runner implements Runnable {
  String URL;
  Client client;
  int numOfTest;
  int population;
  int days;
  int[] timeIntervals;
  private CountDownLatch latch;
  Metrics metrics;

  public Runner(String URL, int numOfTest, int population, int days,
                int[] timeInterval, CountDownLatch latch, Metrics metrics){
    this.URL = URL;
    this.numOfTest = numOfTest;
    this.population = population;
    this.days = days;
    this.timeIntervals = timeInterval;
    this.latch = latch;
    this.metrics = metrics;
    this.client = Client.create();
  }

  public void run() {
    int intervalLength = timeIntervals[1] - timeIntervals[0];
    int times = numOfTest * intervalLength;
    int intervalMin = timeIntervals[0];
    int[] userId = new int[3];
    int[] timeInterval = new int[3];
    int[] stepCount = new int[3];
    for(int i = 1; i <= times; i++){
      for(int j = 0; j < 3; j++){
        userId[j] = ThreadLocalRandom.current().nextInt(population);
        timeInterval[j] = ThreadLocalRandom.current().nextInt(intervalLength) + intervalMin;
        stepCount[j] = ThreadLocalRandom.current().nextInt(1000);
      }
//      Random random = new Random();
//      for(int j = 0; j < 3; j++){
//        userId[j] = random.nextInt(population);
//        timeInterval[j] = random.nextInt(intervalLength) + intervalMin;
//        stepCount[j] = random.nextInt(1000);
//      }
      int day = ThreadLocalRandom.current().nextInt(days);
      sendPostRequest(client, userId[0], day, timeInterval[0], stepCount[0]);
      sendPostRequest(client, userId[1], day, timeInterval[1], stepCount[1]);
      sendGetCurrentRequest(client, userId[0]);
      sendGetSingleRequest(client, userId[1], day);
      sendPostRequest(client ,userId[2], day, timeInterval[2], stepCount[2]);
    }
    latch.countDown();
    long currentcount = latch.getCount();
    System.out.println(currentcount);
  }

  public void sendGetSingleRequest(Client client, int userId, int day){
    long startTime = System.currentTimeMillis();
    String getSingleReuquest = URL + "single/" + userId + "/" + day;
    WebResource webResource = client.resource(getSingleReuquest);
    ClientResponse response = webResource.type("application/json")
            .get(ClientResponse.class);
    if(response.getStatus() == 200) metrics.successfulRequest.getAndIncrement();
    //System.out.println("Get Single " + response.getStatus());
    response.close();
    long endTime = System.currentTimeMillis();
    addToMetric(startTime, endTime);
  }

  public void sendPostRequest(Client client, int userId, int day, int timeInterval, int stepCount){
    long startTime = System.currentTimeMillis();
    String postRequest = URL + "post/" + userId + "/" + day + "/"
            + timeInterval + "/" + stepCount;
    WebResource webResource = client.resource(postRequest);
    ClientResponse response = webResource.type("application/json")
            .post(ClientResponse.class);
    if(response.getStatus() == 201) metrics.successfulRequest.getAndIncrement();
    //System.out.println("Post " + response.getStatus());
    if (response.getStatus() != 201) {
      throw new RuntimeException("Failed : HTTP error code : "
              + response.getStatus());
    }
    String output = response.getEntity(String.class);
    response.close();
    long endTime = System.currentTimeMillis();
    addToMetric(startTime, endTime);
  }

  public void sendGetCurrentRequest(Client client, int userId) {
    long startTime = System.currentTimeMillis();
    String getCurrentReuquest = URL + "current/" + userId;
    WebResource webResource = client.resource(getCurrentReuquest);
    ClientResponse response = webResource.type("application/json")
            .get(ClientResponse.class);
    if(response.getStatus() == 200) metrics.successfulRequest.getAndIncrement();
    //System.out.println("Get current " + response.getStatus());
    response.close();
    long endTime = System.currentTimeMillis();
    addToMetric(startTime, endTime);
  }

  public void addToMetric(long startTime, long endTime){
    int duration = (int)((endTime - metrics.globalStartTime) / 1000);
    metrics.graph.putIfAbsent(duration, 1);
    metrics.graph.put(duration, metrics.graph.get(duration) + 1);
    metrics.latency.add(endTime - startTime);
  }
}