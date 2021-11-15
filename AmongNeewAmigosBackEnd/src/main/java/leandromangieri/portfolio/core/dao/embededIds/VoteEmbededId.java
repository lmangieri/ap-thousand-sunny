package leandromangieri.portfolio.core.dao.embededIds;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class VoteEmbededId implements Serializable {
    public VoteEmbededId() {
    }

    public VoteEmbededId(String personEmail, long pollId) {
        this.personEmail = personEmail;
        this.pollId = pollId;
    }

    @Column(name = "person_email")
    private String personEmail;

    @Column(name = "poll_id")
    private long pollId;
}
