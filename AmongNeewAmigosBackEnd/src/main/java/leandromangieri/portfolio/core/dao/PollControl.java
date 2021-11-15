package leandromangieri.portfolio.core.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="PollControl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollControl implements Serializable {

    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @Id
    private int identifier = 1;

    private int status;


    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "last_poll_id")
    private Poll lastPoll;
}
