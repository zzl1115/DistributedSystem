package neu.edu.cs6650;

import org.glassfish.jersey.client.ClientConfig;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientThread extends Thread {
  private CountDownLatch latch;
  private String url;
  private Integer day;
  private Integer userNum;
  private Integer iteration;
  private Measure measure;
  private Client client;


  public ClientThread(CountDownLatch countDownLatch, String name, String url,
                      Integer day, Integer userNum, Integer iteration, Measure measure) {
    super(name);
    this.latch = countDownLatch;
    this.url = url;
    this.day = day;
    this.userNum = userNum;
    this.iteration = iteration;
    this.client = ClientBuilder.newClient(new ClientConfig());
    this.measure = measure;
  }

  public void post(String path) {
    long startTime = System.currentTimeMillis();
    WebTarget webTarget = client.target(url).path(path);
    Invocation.Builder ib = webTarget.request();
    Response response  = ib.post(null);
//    int status = response.getStatus();
//    if(status == 200) System.out.println("+");
    measure.requests.getAndIncrement();
    response.close();
    long endTime = System.currentTimeMillis();
    addToMeasure(startTime, endTime);
//    System.out.println(1);
  }

  public void get(String path) {
    long startTime = System.currentTimeMillis();
    WebTarget webTarget = client.target(url).path(path);
    Invocation.Builder ib = webTarget.request();
    Response response = ib.get();
//    int status = response.getStatus();
//    if(status == 200) System.out.println("!");
    response.close();
    measure.requests.getAndIncrement();
    long endTime = System.currentTimeMillis();
    addToMeasure(startTime, endTime);
  }

  public String generatePost(Integer user) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    Integer timeInterval = random.nextInt(24);
    Integer step = random.nextInt(5000);
    String res = user + "/" + day + "/" + timeInterval + "/" + step;
    return res;
  }

  public void addToMeasure(long startTime, long endTime){
    int duration = (int)((endTime - measure.globalStartTime) / 1000);
    measure.throughput.put(duration, measure.throughput.getOrDefault(duration, 0) + 1);
    measure.latency.add(endTime - startTime);
  }




  @Override
  public void run() {
    ThreadLocalRandom random = ThreadLocalRandom.current();

    for(int i = 0; i < iteration; i++) {
      Integer user1 = random.nextInt(userNum) + 1;
      Integer user2 = random.nextInt(userNum) + 1;
      Integer user3 = random.nextInt(userNum) + 1;
      post(generatePost(user1));
      post(generatePost(user2));
      get("current/" + user1);
      get("single/" + user2 + "/" + day);
      post(generatePost(user3));
    }
    latch.countDown();
    client.close();

  }
}
