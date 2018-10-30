package neu.edu.cs6650.proj_2_server;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserController {

  @Autowired
  private UserRepository users;
//
//  @Autowired
//  private StepByDayRepository stepByDayRepository;

  @ResponseBody
  @PostMapping(value = "{id}/{day}/{timeInterval}/{stepCount}")
    public void postStep (@PathVariable("id") int id, @PathVariable("day") int day,
                          @PathVariable("timeInterval") int timeInterval, @PathVariable("stepCount") int step) {
      User user = new User();
      user.setUserId(id);
      user.setDay(day);
      user.setTimeInterval(timeInterval);
      user.setStepCount(step);
      try {
        users.save(user);
      } catch (Exception e) {}
    }

    @GetMapping(value = "/current/{id}")
    public Integer getCurStep(@PathVariable("id") int id){
      List<User> list = users.findAllByUserId(id);
      int count = 0;
      if(list.isEmpty()) return count;
      for(User user : list) {count += user.getStepCount();}
      return count;
    }

    @GetMapping(value = "/single/{id}/{day}")
    public Integer getStepByDay(@PathVariable("id") int id, @PathVariable("day") int day) {
      List<User> list = users.findAllByUserIdAndDay(id, day);
      int count = 0;
      for(User user : list)
        count += user.getStepCount();
      return  count;
    }



//  @PostMapping(value = "/create/{userNum}")
//  public void createUsers(@PathVariable("userNum") Integer userNum) {
//    for(int i = 0; i < userNum; i++) {
//      User user = new User();
//      users.saveAndFlush(user);
//    }
//  }
//
//  @PostMapping(value = "{id}/{day}/{timeInterval}/{stepCount}")
//  public void postStep (@PathVariable("id") int id, @PathVariable("day") Integer day,
//                       @PathVariable("stepCount") Integer step) {
//    User user = users.findById(id);
//    if(user == null) {
//      user = new User(id);
//      try {
//        user.setStepSum(step + user.getStepSum());
//      } catch (Exception exception) {
//        throw new RuntimeException(exception);
//      }
//    } else {
//      try {
//        user.setStepSum(step + user.getStepSum());
//      } catch (Exception exception) {
//        throw new RuntimeException(exception);
//      }
//    }
//    StepByDay stepByDay = stepByDayRepository.getByIdAndAndDay(id, day);
//    if(stepByDay == null) {
//      stepByDay = new StepByDay(id, step, day);
//      try {
//        stepByDayRepository.save(stepByDay);
//      } catch (Exception exception) {
//        throw new RuntimeException(exception);
//      }
//    } else
//      stepByDay.setStep(stepByDay.getStep() + step);
//  }
//
//  @GetMapping(value = "/current/{id}")
//  public Integer getCurStep(@PathVariable("id") int id) throws RuntimeException{
//    User res = users.findById(id);
//    if (res == null)
//      return 0;
//    return res.getStepSum();
//  }
//
//  @GetMapping(value = "/single/{id}/{day}")
//  public Integer getStepByDay(@PathVariable("id") Integer id, @PathVariable("day") Integer day) {
//    StepByDay stepByDay = stepByDayRepository.getByIdAndAndDay(id, day);
//    if(stepByDay == null)
//      return 0;
//    return stepByDay.getStep();
//  }
//
//  @GetMapping(value = "range/{id}/{startDay}/{numDays}")
//  public Integer getByRange(@PathVariable("id") Integer id, @PathVariable("startDay") Integer startDay,
//                            @PathVariable("numDays") Integer numDays) {
//    return null;
//  }
}
