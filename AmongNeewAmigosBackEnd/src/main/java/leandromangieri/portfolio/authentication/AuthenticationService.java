package leandromangieri.portfolio.authentication;

import com.amazonaws.services.cognitoidp.model.SignUpResult;
import leandromangieri.portfolio.authentication.dto.LoginRequestAmong;
import leandromangieri.portfolio.authentication.dto.LoginResponse;
import leandromangieri.portfolio.authentication.dto.SignupRequestAmong;


public interface AuthenticationService {
    SignUpResult signUp(SignupRequestAmong signupRequestAmong);
    LoginResponse login(LoginRequestAmong loginRequestAmong2);
    LoginResponse anonymousLogin(String nickName);
}
