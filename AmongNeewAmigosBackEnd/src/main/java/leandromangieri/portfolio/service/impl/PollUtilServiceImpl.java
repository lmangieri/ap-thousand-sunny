package leandromangieri.portfolio.service.impl;

import leandromangieri.portfolio.core.constants.ApplicationConstants;
import leandromangieri.portfolio.core.dao.PollControl;
import leandromangieri.portfolio.core.dao.PollOption;
import leandromangieri.portfolio.core.repository.PollControlRepository;
import leandromangieri.portfolio.core.repository.PollOptionRepository;
import leandromangieri.portfolio.service.PollUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@EnableCaching
public class PollUtilServiceImpl {
    @Autowired
    private PollControlRepository pollControlRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Cacheable("pollControl")
    public PollControl getPollControl() {
        System.out.println("método com cache");
        Optional<PollControl> optionalPollControl = pollControlRepository.findById(1);
        if(optionalPollControl.isPresent()) {
            return optionalPollControl.get();
        } else {
            PollControl pollControl = new PollControl();
            pollControl.setIdentifier(1);
            pollControl.setStatus(ApplicationConstants.POLL_CONTROL_NEVER_USED);
            return pollControl;
        }
    }

    @Cacheable("pollOptions")
    public List<PollOption> getPollOptionsByPollId(long pollId) {
        return this.pollOptionRepository.findAllByPollId(pollId);
    }
}
