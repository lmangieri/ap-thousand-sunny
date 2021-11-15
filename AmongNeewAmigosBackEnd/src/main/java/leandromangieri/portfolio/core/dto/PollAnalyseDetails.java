package leandromangieri.portfolio.core.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PollAnalyseDetails {
    private Map<String, Integer> resultsWithoutIpFilter;
    private int totalVotesWithoutIpFilter;

    private Map<String, Integer> resultsWithIpFilter;
    private int totalVotesWithIpFilter;

    private String voteTitle;
    private List<String> userVotes;
}
