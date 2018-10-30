package neu.edu.cs6650.proj_2_server;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
  List<User> findAllByUserId(int id);
  List<User> findAllByUserIdAndDay(int id, int day);
}
