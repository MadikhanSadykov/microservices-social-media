package com.madikhan.profilemicro.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madikhan.profilemicro.model.entity.Profile;
import com.madikhan.profilemicro.model.request.LoginRequest;
import com.madikhan.profilemicro.service.impl.ProfileServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ProfileServiceImpl profileService;
    private final Environment environment;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                ProfileServiceImpl profileService, Environment environment) {
        super(authenticationManager);
        this.profileService = profileService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword(),
                            new ArrayList<>()
                    ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String username = ( (User) authResult.getPrincipal()).getUsername();
        Profile profile = profileService.getByEmail(username);

        String token = Jwts.builder()
                .setSubject(profile.getUuid())
                .setExpiration(
                        new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration.time")))
                )
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();

        response.addHeader("token", token);
        response.addHeader("uuid", profile.getUuid());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{" + "\n"
                    + "\"" + "token" + "\"" + ":" + "\"" + token + "\"," + "\n"
                    + "\"" + "username" + "\"" + ":" + "\"" + profile.getUsername() + "\"," + "\n"
                    + "\"" + "uuid" + "\"" + ":" + "\"" + profile.getUuid() + "\"" + "\n"
                    + "}"
        );
    }
}
