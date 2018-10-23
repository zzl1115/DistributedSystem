package cs6650;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class MyClient {
  public WebTarget webTarget;
  private int count;
  private int count1;
  private int request;
  private int request1;
  public MyClient(WebTarget webTarget) {
    this.webTarget = webTarget;
    count1 = 0;
    request1 = 0;
    count = 0;
    request = 0;
  }
  public void get() throws IOException{
    Response response =  webTarget.request(MediaType.TEXT_PLAIN).get();
    int status = response.getStatus();
    request1++;
    if (status == 200) {
      count1++;
    }
  }

  public void postText() {
    Response response = webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN)
            .post(Entity.entity("\"sent it\"", MediaType.TEXT_PLAIN));
    int status = response.getStatus();
    request++;
    if (status == 200) {
      count++;
    }
  }
  public int getCount(){
    return count + count1;
  }
  public int getRequest() {
    return request + request1;
  }
}
