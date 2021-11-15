package leandromangieri.portfolio.core.dao;

import leandromangieri.portfolio.core.dao.embededIds.PollOptionEmbededId;
import leandromangieri.portfolio.core.dao.embededIds.VoteEmbededId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Vote")
@Data
@AllArgsConstructor
@NoArgsConstructor
@NamedStoredProcedureQuery(
        name = Vote.NamedQuery_ConsolidateVotes,
        procedureName = "consolidatePoll"
)
public class Vote implements Serializable {
    public static final String NamedQuery_ConsolidateVotes = "consolidateVotes";

    @EmbeddedId
    private VoteEmbededId voteEmbededId;

    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "poll_id", insertable=false, updatable=false)
    private Poll poll;

    private Integer optionNumber;

    private long voteDate;

    private String ipDetails;

    @Column(columnDefinition = "boolean default false")
    private boolean ignoredByIpRule;

    private String nickName;
}
