import com.mchange.v2.c3p0.ComboPooledDataSource;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.Executor;

@RestController
@EnableAutoConfiguration
public class Example {

  public Example() throws PropertyVetoException, SQLException, IOException {
  }

  @GetMapping
  @RequestMapping(value = "/single/{id}/{day}", method = RequestMethod.GET)
  public ResponseEntity<Integer> getUser(@PathVariable("id") int id,
                                         @PathVariable("day") int day)
          throws SQLException, IOException, PropertyVetoException {
    int userStep = getSingleStepFromDB(id, day);
    return new ResponseEntity<Integer>(userStep, HttpStatus.OK);
  }

  public int getSingleStepFromDB(int id, int day)
          throws SQLException, IOException, PropertyVetoException {
//    ConnectionManager cm = new ConnectionManager();
//    Connection connection = cm.getConnection();
    Connection connection = null;
    connection = DataSource.getInstance().getConnection();
    Statement stmt = null;
    stmt = connection.createStatement();
    String findById = "SELECT" +
            "    SUM(step_count)" +
            " FROM" +
            "    user" +
            " WHERE" +
            "    user_id = " + id +
            " And" +
            "    days = " + day;
    //System.out.println(findById);
    ResultSet rs = stmt.executeQuery(findById);
    int result = 0;
    while(rs.next()){
      result += rs.getInt("SUM(step_count)");
    }
    connection.close();
    //System.out.println(result);
    return result;
  }

  @GetMapping
  @RequestMapping(value = "/current/{id}", method = RequestMethod.GET)
  public ResponseEntity<Integer> getUser(@PathVariable("id") int id)
          throws SQLException, IOException, PropertyVetoException {
    int userStep = getCurrentStepFromDB(id);
    return new ResponseEntity<Integer>(userStep, HttpStatus.OK);
  }

  public int getCurrentStepFromDB(int id) throws SQLException, IOException, PropertyVetoException {
    //ConnectionManager cm = new ConnectionManager();
    //Connection connection = cm.getConnection();
    Connection connection = null;
    connection = DataSource.getInstance().getConnection();
    Statement stmt = null;
    stmt = connection.createStatement();
    String findById = "SELECT" +
                      "    SUM(step_count)" +
                      " FROM" +
                      "    user" +
                      " WHERE" +
                      "    user_id = " + id;
    ResultSet rs = stmt.executeQuery(findById);
    int result = 0;
    while(rs.next()){
      result += rs.getInt("SUM(step_count)");
    }
    connection.close();
    //System.out.println(result);
    return result;
  }

//  @PostMapping
//  @RequestMapping(value = "/post", method = RequestMethod.POST)
//  public ResponseEntity< String > persistPerson(@RequestBody Map<String, Object> payload) throws SQLException {
//    int userId = Integer.valueOf((String)payload.get("user_id"));
//    int days = Integer.valueOf((String)payload.get("days"));
//    int timeInterval = Integer.valueOf((String)payload.get("time_interval"));
//    int stepCount = Integer.valueOf((String)payload.get("step_count"));
//    sendToDB(userId, days, timeInterval, stepCount);
//    return ResponseEntity.status(HttpStatus.CREATED).build();
//  }

  @PostMapping
  @RequestMapping(value = "/post/{id}/{days}/{timeInterval}/{stepCount}", method = RequestMethod.POST)
  public ResponseEntity< String > persistPerson(@PathVariable("id") int id, @PathVariable("days") int days
  ,@PathVariable("timeInterval") int timeInterval,@PathVariable("stepCount") int stepCount)
          throws SQLException, IOException, PropertyVetoException {
    //System.out.println(id);
    sendToDB(id, days, timeInterval, stepCount);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  public void sendToDB(int userId, int days, int timeInterval, int stepCount)
          throws SQLException, IOException, PropertyVetoException {
//    ConnectionManager cm = new ConnectionManager();
//    Connection connection = cm.getConnection();
    Connection connection = null;
    connection = DataSource.getInstance().getConnection();
    Statement stmt = null;
    stmt = connection.createStatement();
    String insertUser = "INSERT IGNORE INTO user(user_id, days, time_interval, step_count)" +
            "VALUES (" + userId + "," + days + "," + timeInterval + "," + stepCount + ")";
    //System.out.println(insertUser);
    stmt.executeUpdate(insertUser);
    connection.close();
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Example.class, args);
  }
}