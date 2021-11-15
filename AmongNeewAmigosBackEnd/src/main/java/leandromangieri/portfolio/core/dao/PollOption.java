package leandromangieri.portfolio.core.dao;

import leandromangieri.portfolio.core.dao.embededIds.PollOptionEmbededId;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="PollOption")
public class PollOption implements Serializable {

    @EmbeddedId
    private PollOptionEmbededId pollOptionEmbededId;

    private String optionDescription;

    private int totalNumberOfVotes;
    private int numberOfVotesWithIpFilter;

    @ManyToOne
    @JoinColumn(name = "poll_id", insertable=false, updatable=false)
    private Poll poll;
}
