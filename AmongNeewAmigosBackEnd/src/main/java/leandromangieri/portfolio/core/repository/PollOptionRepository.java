package leandromangieri.portfolio.core.repository;

import leandromangieri.portfolio.core.dao.PollOption;
import leandromangieri.portfolio.core.dao.embededIds.PollOptionEmbededId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PollOptionRepository extends JpaRepository<PollOption, PollOptionEmbededId> {
    @Query("FROM PollOption PO where PO.pollOptionEmbededId.pollId = :pollId")
    List<PollOption> findAllByPollId(@Param("pollId") long pollId);
}
