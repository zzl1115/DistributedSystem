public class InputData {
  int userId;
  int days;
  int timeInterval;
  int stepCount;

  public InputData(){}

  public InputData(int userId, int days, int timeInterval, int stepCount){
    this.userId = userId;
    this.days = days;
    this.timeInterval = timeInterval;
    this.stepCount = stepCount;
  }

  public void setDays(int days) {
    this.days = days;
  }

  public void setStepCount(int stepCount) {
    this.stepCount = stepCount;
  }

  public void setTimeInterval(int timeInterval) {
    this.timeInterval = timeInterval;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }
}
