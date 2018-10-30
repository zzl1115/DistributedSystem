//package neu.edu.cs6650.proj_2_server;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//@Entity
//@Table(name = "step_by_day")
//public class StepByDay {
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Integer id;
//  private Integer day;
//  private Integer step;
//
////  @ManyToOne
////  @JoinColumn (name = "id")
////  private User user;
//
//  public StepByDay(){
//  }
//
//  public StepByDay(Integer id, Integer step, Integer day) {
//    super();
//    this.id = id;
//    this.step = step;
//    this.day = day;
//  }
//
//  public Integer getDay() {
//    return day;
//  }
//
//  public void setDay(Integer day) {
//    this.day = day;
//  }
//
//  public Integer getStep() {
//    return step;
//  }
//
//  public void setStep(Integer step) {
//    this.step = step;
//  }
//}
