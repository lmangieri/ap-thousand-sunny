package leandromangieri.portfolio.service;

import leandromangieri.portfolio.core.dto.PollDto;
import leandromangieri.portfolio.core.dto.VoteResponse;

public interface PollService {
    String createPoll(PollDto pollDto);
    VoteResponse vote(Integer option, String ipDetails);

    String analysePoll();
    String endPoll(int option);

    String returnPollToOpenState();
}
