package leandromangieri.portfolio.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

// http://localhost:8080/teste123/teste
@RestController
@RequestMapping(path = "teste123")
public class ApplicationController {


    @PreAuthorize("hasAnyRole('ROLE_COMMON_P')")
    @GetMapping(value = "teste")
    public String teste(HttpServletRequest request) {
        System.out.println("Test end point leandro foi chamado - ApplicationController");

        //authenticationService.isUserLogged(accessToken);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            String email = ((UserDetails)principal).getUsername();
            System.out.println(email);
        }

        StringBuilder strBuilder = new StringBuilder();
        if (request != null) {
            strBuilder.append("X-FORWARDED-FOR -> " + request.getHeader("X-FORWARDED-FOR"));
            strBuilder.append("| remoteAddr -> " + request.getRemoteAddr());

        }
        return strBuilder.toString();

    }
}
