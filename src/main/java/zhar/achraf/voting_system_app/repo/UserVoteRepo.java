package zhar.achraf.voting_system_app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import zhar.achraf.voting_system_app.entity.UserVote;

import java.util.Optional;

public interface UserVoteRepo extends JpaRepository<UserVote, Long> {
    // Check if a user has already voted on a poll
    Optional<UserVote> findByUserIdAndPollId(Long userId, Integer pollId);
}