package leandromangieri.portfolio.core.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/*Poll means Votação*/
@Entity
@Table(name="Poll")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Poll implements Serializable {
    @Id
    @GeneratedValue
    private long pollId; /* Preciso fazer que isso fique compatível com o banco que vou usar*/

    private long creationDateInMillis;

    private String creator;

    private String title;

    @OneToMany(mappedBy = "poll", fetch = FetchType.EAGER)
    private List<PollOption> pollOptionList;

    /*1 => Votação Sem Restrições de IP*/
    /*2 => Votação Com Restrições de IP*/
    private int finalizationType;

}
