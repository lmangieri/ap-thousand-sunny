package leandromangieri.portfolio.api;

import leandromangieri.portfolio.core.dto.*;
import leandromangieri.portfolio.core.errors.exceptions.BadRequestException;
import leandromangieri.portfolio.service.HomeService;
import leandromangieri.portfolio.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://www.thousand-sunny-games.net","https://thousand-sunny-games.net","https://ap.thousand-sunny-games.net"})
@RequestMapping(path = "app")
public class PollController {
    @Autowired
    private PollService pollService;

    @Autowired
    private HomeService homeService;


    @PreAuthorize("hasAnyRole('ROLE_ADMIN_P')")
    @PostMapping(value = "poll")
    public GenericResponse createPoll(@Valid @RequestBody PollDto pollDto) {
        String msg = pollService.createPoll(pollDto);
        GenericResponse genericResponse = new GenericResponse();
        return genericResponse;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_P')")
    @PostMapping(value = "analysePoll")
    public String analysePoll() {
        return pollService.analysePoll();
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN_P')")
    @PostMapping(value = "returnPollToOpenState")
    public GenericResponse returnPollToOpenState() {
        String result = pollService.returnPollToOpenState();
        return new GenericResponse(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_P')")
    @PostMapping(value = "endPoll")
    public String endPoll(@RequestParam(name="option") int option) {
        if(option != 1 && option != 2) {
            throw new BadRequestException("Invalid option to end Poll");
        }
        return pollService.endPoll(option);
    }

    @PreAuthorize("hasAnyRole('ROLE_COMMON_P')")
    @PostMapping(value = "vote")
    public VoteResponse vote(@RequestParam(name="option") Integer option, HttpServletRequest request) {
        String ipDetails = this.getIpDetails(request);
        return pollService.vote(option, ipDetails);
    }

    private String getIpDetails(HttpServletRequest request) {
        StringBuilder strBuilder = new StringBuilder();
        if (request != null) {
            strBuilder.append("X-FORWARDED-FOR -> " + request.getHeader("X-FORWARDED-FOR"));
            strBuilder.append("| remoteAddr -> " + request.getRemoteAddr());
        }
        return strBuilder.toString();
    }

    @PreAuthorize("hasAnyRole('ROLE_COMMON_P')")
    @GetMapping(value = "home")
    public HomeDetailsDto getHomeDetails() {
        return homeService.getHomeDetails();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN_P')")
    @GetMapping(value = "home/analysePoll")
    public PollAnalyseDetails getPollAnalyseDetails() {
        return homeService.getPollAnalyseDetails();
    }
}
