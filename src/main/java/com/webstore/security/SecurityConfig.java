package com.webstore.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessHandler successHandler;
    private final UserDetailsService userDetailsService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/registration", "/login", "/logout", "/oauth2/authorization/google").permitAll() // доступность всем
                .antMatchers("/admin/**").access("hasAnyRole('ROLE_ADMIN')")
                .antMatchers("/user/**").access("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR','ROLE_ADMIN')")
                .antMatchers("/moderator/**").access("hasAnyRole('ROLE_MODERATOR', 'ROLE_ADMIN')")
//                .anyRequest().authenticated()
                .and().formLogin()
//                .loginPage("/login")
                .successHandler(successHandler)
                .loginProcessingUrl("/login")
                .usernameParameter("email")
//                .passwordParameter("password")
//                .permitAll()
//                .and()
//                .oauth2Login().loginPage("/oauth2/authorization/google")
//                .successHandler(successHandler)
                .and().csrf().disable();//- Защита CSRF включена по умолчанию в конфигурации Java.

        http.logout()//URL выхода из системы безопасности Spring - только POST. Вы можете поддержать выход из системы без POST, изменив конфигурацию Java
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))//выход из системы гет запрос на /logout
                .logoutSuccessUrl("/")//успешный выход из системы
                .and().csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }
}