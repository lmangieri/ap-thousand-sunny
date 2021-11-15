package leandromangieri.portfolio.core.dto;

import lombok.Data;

import java.util.Map;

@Data
public class HomeDetailsDto {
    private int pollControlStatus;
    private String voteTitle;
    private Map<Integer,String> voteOptions;
    private boolean hasUserVoted;
    private String userVote;

    private String titleLastPoll;
    private Map<String, Integer> resultsLastPoll;
    private int totalVotes;
}
