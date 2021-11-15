package leandromangieri.portfolio.service;

import leandromangieri.portfolio.core.dto.HomeDetailsDto;
import leandromangieri.portfolio.core.dto.PollAnalyseDetails;

public interface HomeService {
    HomeDetailsDto getHomeDetails();
    PollAnalyseDetails getPollAnalyseDetails();
}
