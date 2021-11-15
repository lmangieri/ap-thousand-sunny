package leandromangieri.portfolio.service.impl;

import leandromangieri.portfolio.App;
import leandromangieri.portfolio.core.constants.ApplicationConstants;
import leandromangieri.portfolio.core.dao.Poll;
import leandromangieri.portfolio.core.dao.PollControl;
import leandromangieri.portfolio.core.dao.PollOption;
import leandromangieri.portfolio.core.dao.Vote;
import leandromangieri.portfolio.core.dao.embededIds.VoteEmbededId;
import leandromangieri.portfolio.core.dto.HomeDetailsDto;
import leandromangieri.portfolio.core.dto.PollAnalyseDetails;
import leandromangieri.portfolio.core.errors.exceptions.BadRequestException;
import leandromangieri.portfolio.core.repository.PollControlRepository;
import leandromangieri.portfolio.core.repository.PollOptionRepository;
import leandromangieri.portfolio.core.repository.PollRepository;
import leandromangieri.portfolio.core.repository.VoteRepository;
import leandromangieri.portfolio.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@EnableCaching
public class HomeServiceImpl implements HomeService {
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


    @Override
    public HomeDetailsDto getHomeDetails() {
        HomeDetailsDto homeDetailsDto = new HomeDetailsDto();

        PollControl pollControl = pollUtilService.getPollControl();

        int status = pollControl.getStatus();

        homeDetailsDto.setPollControlStatus(status);


        if(status == ApplicationConstants.POLL_CONTROL_POLL_CREATED) {
            Poll poll = pollControl.getPoll();
            homeDetailsDto.setVoteTitle(poll.getTitle());
            Map<Integer,String> voteOptions = new HashMap<>();
            for(PollOption option : poll.getPollOptionList()) {
                voteOptions.put(option.getPollOptionEmbededId().getOptionNumber(),option.getOptionDescription());
            }
            homeDetailsDto.setVoteOptions(voteOptions);
            VoteEmbededId voteEmbededId = new VoteEmbededId(getUserEmail(), poll.getPollId());
            Optional<Vote> optionalVote = voteRepository.findById(voteEmbededId);
            if(optionalVote.isPresent()) {
                homeDetailsDto.setHasUserVoted(true);
                homeDetailsDto.setUserVote(voteOptions.get(optionalVote.get().getOptionNumber()));
            } else {
                homeDetailsDto.setHasUserVoted(false);
            }
        }

        if(status == ApplicationConstants.POLL_CONTROL_ENDED) {
            Poll poll = pollControl.getLastPoll();
            if(poll == null) {
                System.out.println("Last poll is null; este fluxo não deveria acontecer...");
                throw new RuntimeException("Last Poll is null and POLL_CONTROL_ENDED is set to true");
            }
            homeDetailsDto.setVoteTitle(poll.getTitle());

            Map<String, Integer> resultsLastPoll = new HashMap<>();
            int totalVotes = 0;

            for(PollOption option : poll.getPollOptionList()) {
                if(poll.getFinalizationType() == 1) {  /*1 => Votação Sem Restrições de IP*/
                    resultsLastPoll.put(option.getOptionDescription(),option.getTotalNumberOfVotes());
                    totalVotes = totalVotes + option.getTotalNumberOfVotes();
                } else { /*2 => Votação Com Restrições de IP*/
                    resultsLastPoll.put(option.getOptionDescription(),option.getNumberOfVotesWithIpFilter());
                    totalVotes = totalVotes + option.getNumberOfVotesWithIpFilter();
                }
            }
            homeDetailsDto.setTotalVotes(totalVotes);
            homeDetailsDto.setResultsLastPoll(resultsLastPoll);
        }
        return homeDetailsDto;
    }



    private String getUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            String email = ((UserDetails)principal).getUsername();
            return email;
        }
        return null;
    }



    @Override
    public PollAnalyseDetails getPollAnalyseDetails() {
        PollAnalyseDetails pollAnalyseDetails = new PollAnalyseDetails();

        PollControl pollControl = pollUtilService.getPollControl();
        int status = pollControl.getStatus();
        if(status == ApplicationConstants.POLL_CONTROL_IN_ANALYSES) {
            List<PollOption> pollOptions = pollUtilService.getPollOptionsByPollId(pollControl.getPoll().getPollId());
            Map<String, Integer> resultsWithoutIpFilter = new HashMap<>();
            Map<String, Integer> resultsWithIpFilter = new HashMap<>();


            int totalVotesWithoutIpFilter = 0;
            for(PollOption pollOption : pollOptions) {
                resultsWithoutIpFilter.put(pollOption.getOptionDescription(), pollOption.getTotalNumberOfVotes());
                totalVotesWithoutIpFilter = totalVotesWithoutIpFilter + pollOption.getTotalNumberOfVotes();
            }

            int totalVotesWithIpFilter = 0;
            for(PollOption pollOption : pollOptions) {
                resultsWithIpFilter.put(pollOption.getOptionDescription(), pollOption.getNumberOfVotesWithIpFilter());
                totalVotesWithIpFilter = totalVotesWithIpFilter + pollOption.getNumberOfVotesWithIpFilter();
            }
            pollAnalyseDetails.setResultsWithIpFilter(resultsWithIpFilter);
            pollAnalyseDetails.setResultsWithoutIpFilter(resultsWithoutIpFilter);
            pollAnalyseDetails.setVoteTitle(pollControl.getPoll().getTitle());
            pollAnalyseDetails.setTotalVotesWithIpFilter(totalVotesWithIpFilter);
            pollAnalyseDetails.setTotalVotesWithoutIpFilter(totalVotesWithoutIpFilter);

            List<Vote> voteList = voteRepository.findAllByPollId(pollControl.getPoll().getPollId());

            /* Pegando estrutura de dados para recuperar o nome do voto a partir de um identificador - inicio */
            Map<Integer,String> voteOptions = new HashMap<>();
            for(PollOption option : pollControl.getPoll().getPollOptionList()) {
                voteOptions.put(option.getPollOptionEmbededId().getOptionNumber(),option.getOptionDescription());
            }
            /*Pegando estrutura de dados para recuperar o nome do voto a partir de um identificador - Fim*/

            /*userVotes é uma lista com uma Stringona, com os detalhes do voto do usuário */
            List<String> userVotes = new ArrayList<>();
            for(Vote vote : voteList) {
                String userVote = "Nick:"+vote.getNickName() +" votou em "+voteOptions.get(vote.getOptionNumber());
                if(vote.isIgnoredByIpRule()) {
                    userVote = userVote + " - Trapaça Identificada";
                }
                userVotes.add(userVote);
            }

            pollAnalyseDetails.setUserVotes(userVotes);

            return pollAnalyseDetails;
        }

        throw new BadRequestException("Poll is not on "+ApplicationConstants.POLL_CONTROL_IN_ANALYSES+" status");
    }

}
