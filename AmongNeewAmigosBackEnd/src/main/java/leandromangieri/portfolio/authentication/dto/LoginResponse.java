package leandromangieri.portfolio.authentication.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class LoginResponse {
    private String nickName;
    private String idToken;
    private Set<String> roles;
    private String expiresIn;
}
