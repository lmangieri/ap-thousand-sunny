package leandromangieri.portfolio.core.repository;


import leandromangieri.portfolio.core.dao.PollOption;
import leandromangieri.portfolio.core.dao.Vote;
import leandromangieri.portfolio.core.dao.embededIds.VoteEmbededId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository  extends JpaRepository<Vote, VoteEmbededId> {
    @Procedure(name = Vote.NamedQuery_ConsolidateVotes)
    void consolidateVotes();


    @Query("FROM Vote V where V.voteEmbededId.pollId = :pollId")
    List<Vote> findAllByPollId(@Param("pollId") long pollId);
}
