package com.webstore.security;

import com.webstore.entity.Account;
import com.webstore.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SuccessHandler implements AuthenticationSuccessHandler {

    private AccountService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

//        if (authentication.toString().contains ("given_name")){
//            Account account = (Account) accountService.loadUserByUsername(((DefaultOidcUser)authentication.getPrincipal()).getEmail());
//            SecurityContextHolder.getContext().setAuthentication(
//                    new UsernamePasswordAuthenticationToken(
//                            SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
//                            SecurityContextHolder.getContext().getAuthentication().getCredentials(),
//                            Collections.singleton(account.getRole())));
//        }

        Set<String> roles = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/admin");
        } else if(roles.contains("ROLE_USER")){
            httpServletResponse.sendRedirect("/user");
        }else if(roles.contains("ROLE_MODERATOR")){
            httpServletResponse.sendRedirect("/moderator");
        }else{
            httpServletResponse.sendRedirect("/welcome");
        }
    }
}