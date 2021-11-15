package leandromangieri.portfolio.core.repository;

import leandromangieri.portfolio.core.dao.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll,Integer> {
}
