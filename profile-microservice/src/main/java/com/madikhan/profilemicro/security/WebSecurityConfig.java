package com.madikhan.profilemicro.security;

import com.madikhan.profilemicro.filter.AuthenticationFilter;
import com.madikhan.profilemicro.service.impl.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final ProfileServiceImpl profileService;
    private final BCryptPasswordEncoder passwordEncoder;

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(authenticationManager(), profileService, environment);
        authenticationFilter.setAuthenticationManager(authenticationManager());
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.uri.path"));
        return authenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/v1/profiles", "/api/v1/login").permitAll()
                .anyRequest().permitAll()
                .and()
                .addFilter(getAuthenticationFilter());

        httpSecurity.cors();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(profileService).passwordEncoder(passwordEncoder);
    }

}
