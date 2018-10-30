import java.util.concurrent.CountDownLatch;

public class ClientThread extends Thread {
  private CountDownLatch latch;
  private String url;
  private Integer iteration;
  private Client

  public ClientThread(CountDownLatch countDownLatch, String name) {
    super(name);
    this.latch = countDownLatch;
  }

  @Override
  public void run() {

  }
}
