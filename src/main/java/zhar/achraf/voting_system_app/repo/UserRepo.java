package zhar.achraf.voting_system_app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import zhar.achraf.voting_system_app.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findTop4ByOrderByCreatedAtDesc();
}
