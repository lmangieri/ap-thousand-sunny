package leandromangieri.portfolio.authentication.impl;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import leandromangieri.portfolio.authentication.AuthenticationService;
import leandromangieri.portfolio.authentication.dto.LoginRequestAmong;
import leandromangieri.portfolio.authentication.dto.LoginResponse;
import leandromangieri.portfolio.authentication.dto.SignupRequestAmong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${application.clientId}")
    private String clientId;

    @Value("${application.userPool}")
    private String userPool;

    @Value("${application.complexAppSecret}")
    private String complexAppSecret;

    @Autowired
    private AWSCognitoIdentityProvider awsCognitoIdentityProvider;

    public SignUpResult signUp(SignupRequestAmong signupRequestAmong) {

        AttributeType attributeType = new AttributeType();
        attributeType.setName("nickname");
        attributeType.setValue(signupRequestAmong.getNickName());

        AttributeType attributeType2 = new AttributeType();
        attributeType2.setName("email");
        attributeType2.setValue(signupRequestAmong.getEmail());

        List<AttributeType> attributeTypeList = new ArrayList<>();
        attributeTypeList.add(attributeType);
        attributeTypeList.add(attributeType2);

        SignUpRequest signUpRequest = new SignUpRequest().withClientId(clientId).withUsername(signupRequestAmong.getEmail()).withPassword(signupRequestAmong.getPassword()).withUserAttributes(attributeTypeList);

        SignUpResult signUpResult = awsCognitoIdentityProvider.signUp(signUpRequest);
        return signUpResult;
    }

    public LoginResponse login(LoginRequestAmong loginRequestAmong2) {
        Map<String,String> authParams = new LinkedHashMap<String,String>(){{
            put("USERNAME", loginRequestAmong2.getEmail());
            put("PASSWORD", loginRequestAmong2.getPassword());
        }};

        AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .withUserPoolId(userPool)
                .withClientId(clientId)
                .withAuthParameters(authParams);
        AdminInitiateAuthResult authResult = awsCognitoIdentityProvider.adminInitiateAuth(authRequest);
        AuthenticationResultType resultType = authResult.getAuthenticationResult();
        return this.createLoginResponse(resultType.getIdToken());
    }


    private LoginResponse createLoginResponse(String idToken) {
        DecodedJWT jwt = JWT.decode(idToken);
        String nickName = jwt.getClaim("nickname").asString();
        String expiresIn = jwt.getClaim("exp").asInt().toString();

        Set<String> roles = new TreeSet<>();

        Claim cognitoGroupsConsolidatedClaim = jwt.getClaim("cognito:groups");
        String[] str = cognitoGroupsConsolidatedClaim.asArray(String.class);
        if(str != null) {
            roles.addAll(Arrays.asList(str));
        }
        roles.add("ROLE_COMMON_P");
        return LoginResponse.builder().idToken(idToken)
                .nickName(nickName)
                .roles(roles)
                .expiresIn(expiresIn)
                .build();
    }


    public LoginResponse anonymousLogin(String nickName) {
        Algorithm algorithm = Algorithm.HMAC256(complexAppSecret);
        long currTimeMillis = System.currentTimeMillis();
        String token = JWT.create()
                .withExpiresAt(Date.from(Instant.now().plusSeconds(60000000)))
                .withClaim("nickname",nickName)
                .withClaim("cognito:groups",Arrays.asList("ROLE_COMMON_P"))
                .withClaim("email","dummy"+this.generateRandomString()+currTimeMillis+"@mail.com")
                .sign(algorithm);
        return this.createLoginResponse(token);
    }

    private String generateRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
