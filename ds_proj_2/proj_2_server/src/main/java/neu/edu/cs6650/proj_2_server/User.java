package neu.edu.cs6650.proj_2_server;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue
  @Column(name = "recordId", nullable = false)
  private int recordId;

  @Column(name = "userId", nullable = false)
  private int userId;

  @Column(name = "day", nullable = false)
  private int day;

  @Column(name = "timeInterval", nullable = false)
  private int timeInterval;

  @Column(name = "stepCount", nullable = false)
  private int stepCount;

  public User(){
    this.userId = 0;
    this.day = 0;
    this.timeInterval = 0;
    this.stepCount = 0;
  }

  public User(int id, int day, int timeInterval, int stepCount) {
    this.userId = id;
    this.day = day;
    this.timeInterval = timeInterval;
    this.stepCount = stepCount;
  }

  public int getRecordId() {
    return recordId;
  }

  public void setRecordId(int recordId) {
    this.recordId = recordId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int id) {
    this.userId = id;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public int getTimeInterval() {
    return timeInterval;
  }

  public void setTimeInterval(int timeInterval) {
    this.timeInterval = timeInterval;
  }

  public int getStepCount() {
    return stepCount;
  }

  public void setStepCount(int stepCount) {
    this.stepCount = stepCount;
  }

  //  @OneToMany(mappedBy = "user")
//  private Set<StepByDay> steps;
//
//  public User() {
//    stepSum = 0;
////    steps = new HashSet<>();
//  }
//
//  public User(int id) {
//    this.id = id;
//    stepSum = 0;
//  }
//
//  public Integer getId() {
//    return id;
//  }
//
//  public void setId(Integer id) {
//    this.id = id;
//  }
//
//  public Integer getStepSum() {
//    return stepSum;
//  }
//
//  public void setStepSum(Integer stepSum) {
//    this.stepSum = stepSum;
//  }

}
