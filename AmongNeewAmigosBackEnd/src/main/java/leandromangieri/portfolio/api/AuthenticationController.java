package leandromangieri.portfolio.api;

import com.amazonaws.services.cognitoidp.model.SignUpResult;
import leandromangieri.portfolio.authentication.AuthenticationService;
import leandromangieri.portfolio.authentication.dto.LoginRequestAmong;
import leandromangieri.portfolio.authentication.dto.LoginResponse;
import leandromangieri.portfolio.authentication.dto.SignupRequestAmong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = {"http://localhost:4200","https://www.thousand-sunny-games.net","https://thousand-sunny-games.net","https://ap.thousand-sunny-games.net"})
@RequestMapping(path = "authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;



    @PostMapping(path="/signUp" , consumes = "application/json" , produces = "application/json")
    public SignUpResult signUpAws(@Valid @RequestBody SignupRequestAmong signupRequest) {
        return authenticationService.signUp(signupRequest);
    }

    @PostMapping(path="/anonymousLogin" , consumes = "application/json" , produces = "application/json")
    public LoginResponse anonymousSignUp(@Valid @RequestParam String nickName) {
        return authenticationService.anonymousLogin(nickName);
    }

    @PostMapping(path="/login",  consumes = "application/json" , produces = "application/json")
    public LoginResponse login(@Valid @RequestBody LoginRequestAmong loginRequestAmong) {
        return authenticationService.login(loginRequestAmong);
    }

}
