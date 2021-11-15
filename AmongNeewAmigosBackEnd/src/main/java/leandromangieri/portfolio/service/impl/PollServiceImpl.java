package leandromangieri.portfolio.service.impl;

import leandromangieri.portfolio.core.constants.ApplicationConstants;
import leandromangieri.portfolio.core.dao.Poll;
import leandromangieri.portfolio.core.dao.PollControl;
import leandromangieri.portfolio.core.dao.PollOption;
import leandromangieri.portfolio.core.dao.Vote;
import leandromangieri.portfolio.core.dao.embededIds.PollOptionEmbededId;
import leandromangieri.portfolio.core.dao.embededIds.VoteEmbededId;
import leandromangieri.portfolio.core.dto.PollDto;
import leandromangieri.portfolio.core.dto.VoteResponse;
import leandromangieri.portfolio.core.errors.exceptions.BadRequestException;
import leandromangieri.portfolio.core.repository.PollControlRepository;
import leandromangieri.portfolio.core.repository.PollOptionRepository;
import leandromangieri.portfolio.core.repository.PollRepository;
import leandromangieri.portfolio.core.repository.VoteRepository;
import leandromangieri.portfolio.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@EnableCaching
public class PollServiceImpl implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    @Autowired
    private PollControlRepository pollControlRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollUtilServiceImpl pollUtilService;


    @Transactional
    private boolean createPollInTransaction(PollDto pollDto, PollControl pollControl) {
        Poll poll = new Poll();

        poll.setCreationDateInMillis(System.currentTimeMillis());

        poll.setCreator(getUserEmail());
        poll.setTitle(pollDto.getTitle());
        pollRepository.save(poll);
        System.out.println(poll.getPollId());
        int i = 1;
        try {
            for(String optionDescription : pollDto.getOptions()) {
                PollOption pollOption = new PollOption();
                pollOption.setPoll(poll);
                pollOption.setPollOptionEmbededId(new PollOptionEmbededId(i,poll.getPollId()));
                pollOption.setOptionDescription(optionDescription);
                pollOptionRepository.save(pollOption);
                i++;
            }
            pollControl.setPoll(poll);
            pollControl.setStatus(ApplicationConstants.POLL_CONTROL_POLL_CREATED);
            pollControlRepository.saveAndFlush(pollControl);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return true;
    }

    @CacheEvict(value = { "pollControl", "pollOptions" }, allEntries = true)
    public String createPoll(PollDto pollDto) {
        PollControl pollControl = pollUtilService.getPollControl();
        if(pollControl.getStatus() == ApplicationConstants.POLL_CONTROL_NEVER_USED || pollControl.getStatus() == ApplicationConstants.POLL_CONTROL_ENDED) {
            createPollInTransaction(pollDto,pollControl);
            return "createPoll success";
        } else {
            throw new BadRequestException("Trying to create Poll, but status is "+pollControl.getStatus());
        }
    }

    @Override
    public VoteResponse vote(Integer option, String ipDetails) {
        PollControl pollControl = pollUtilService.getPollControl();
        if(pollControl.getStatus() == ApplicationConstants.POLL_CONTROL_POLL_CREATED) {
            Poll poll = pollControl.getPoll();
            boolean existsOption = pollOptionRepository.existsById(new PollOptionEmbededId(option,poll.getPollId()));
            if(existsOption) {
                Vote vote = new Vote();
                vote.setVoteEmbededId(new VoteEmbededId(getUserEmail(),poll.getPollId()));
                vote.setOptionNumber(option);
                vote.setVoteDate(System.currentTimeMillis());
                vote.setIpDetails(ipDetails);
                vote.setNickName(getNickName());
                voteRepository.save(vote);
            } else {
                throw new BadRequestException("Opção inválida");
            }
        } else {
            throw new BadRequestException("Can't vote now");
        }

        VoteResponse voteResponse = new VoteResponse();
        voteResponse.setResult("success vote");

        return voteResponse;
    }

    @Override
    @CacheEvict(value = { "pollControl", "pollOptions" }, allEntries = true)
    public String returnPollToOpenState() {
        PollControl pollControl = pollUtilService.getPollControl();
        if(pollControl.getStatus() == ApplicationConstants.POLL_CONTROL_IN_ANALYSES) {
            pollControl.setStatus(ApplicationConstants.POLL_CONTROL_POLL_CREATED);
            pollControlRepository.saveAndFlush(pollControl);
            return "Poll returned to open state successfully";
        }
        throw new BadRequestException("To return Poll to Open State, needs to be on status "+ApplicationConstants.POLL_CONTROL_IN_ANALYSES+". Trying to return poll to open state, but status is "+pollControl.getStatus());
    }


    @Override
    @CacheEvict(value = { "pollControl", "pollOptions" }, allEntries = true)
    public String analysePoll() {
        PollControl pollControl = pollUtilService.getPollControl();
        if(pollControl.getStatus() == ApplicationConstants.POLL_CONTROL_POLL_CREATED) {
            pollControl.setStatus(ApplicationConstants.POLL_CONTROL_IN_ANALYSES);
            pollControlRepository.saveAndFlush(pollControl);
            voteRepository.consolidateVotes();
            return "analyse poll called successfully";
        }
        throw new BadRequestException("To be analysed, needs to be on status "+ApplicationConstants.POLL_CONTROL_POLL_CREATED+". Trying to analyse Poll, but status is "+pollControl.getStatus());
    }

    @Override
    @CacheEvict(value = { "pollControl", "pollOptions" }, allEntries = true)
    public String endPoll(int option) {
        PollControl pollControl = pollUtilService.getPollControl();
        if(pollControl.getStatus() == ApplicationConstants.POLL_CONTROL_IN_ANALYSES) {
            Poll poll = pollControl.getPoll();
            poll.setFinalizationType(option);
            pollControl.setLastPoll(poll);
            pollControl.setStatus(ApplicationConstants.POLL_CONTROL_ENDED);
            pollControl.setPoll(null);
            pollControlRepository.saveAndFlush(pollControl);
            pollRepository.saveAndFlush(poll);
            return "endPoll called successfully";
        }
        throw new BadRequestException("To be ended, needs to be on status "+ApplicationConstants.POLL_CONTROL_IN_ANALYSES+" Poll is on "+pollControl.getStatus()+" status");
    }


    private String getUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            String email = ((UserDetails)principal).getUsername();
            return email;
        }
        return null;
    }

    private String getNickName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nick = (String)authentication.getDetails();
        return nick;
    }
}
