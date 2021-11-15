package leandromangieri.portfolio.core.dao.embededIds;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class PollOptionEmbededId implements Serializable {
    public PollOptionEmbededId() {
    }

    public PollOptionEmbededId(Integer optionNumber,long pollId) {
        this.optionNumber = optionNumber;
        this.pollId = pollId;
    }

    @Column(name = "option_number")
    private Integer optionNumber;

    @Column(name = "poll_id")
    private long pollId;
}
