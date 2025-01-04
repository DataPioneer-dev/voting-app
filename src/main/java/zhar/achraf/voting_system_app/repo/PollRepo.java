package zhar.achraf.voting_system_app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import zhar.achraf.voting_system_app.entity.Poll;

public interface PollRepo extends JpaRepository<Poll, Integer> {

}
