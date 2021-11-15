package leandromangieri.portfolio.authentication.filters;

import com.auth0.jwk.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Value("${application.urlJwkProvider}")
    private String urlJwkProvider;

    @Value("${application.complexAppSecret}")
    private String complexAppSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.resolveToken((HttpServletRequest) request);
        if (token != null && this.validateTokenAWS(token)) {
            DecodedJWT jwt = JWT.decode(token);
            Claim cognitoGroupsConsolidatedClaim = jwt.getClaim("cognito:groups");
            String[] str = cognitoGroupsConsolidatedClaim.asArray(String.class);
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            if(str != null) {
                for(String role : str) {
                    grantedAuthorityList.add(new SimpleGrantedAuthority(role));
                }
            }
            grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_COMMON_P"));

            User principal = new User(jwt.getClaim("email").asString(),"",grantedAuthorityList);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(principal,"",grantedAuthorityList);
            usernamePasswordAuthenticationToken.setDetails(jwt.getClaim("nickname").asString());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);

    }


    private String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private boolean validateTokenAWS(String token) {
        if(validateAnonymousToken(token)) {
            return true;
        }

        try {
            DecodedJWT jwt = JWT.decode(token);
            JwkProvider provider = new UrlJwkProvider(this.urlJwkProvider);
            Jwk jwk = provider.get(jwt.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);
            if(jwt.getExpiresAt().compareTo(new Date()) == -1) {
                System.out.println("token has expired");
                return false;
            }
            return true;
        } catch (JWTDecodeException ex) {
            ex.printStackTrace();
            // geralmente quando o token não tem as 3 partes corretas.
        }
        catch (SigningKeyNotFoundException ex) {
            ex.printStackTrace();
        } catch (JwkException ex) {
            ex.printStackTrace();
        } catch (SignatureVerificationException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    private boolean validateAnonymousToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(complexAppSecret);
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .build(); //Reusable verifier instance
            verifier.verify(token);
        } catch (JWTVerificationException exception){
            return false;
        }
        return true;
    }

}
