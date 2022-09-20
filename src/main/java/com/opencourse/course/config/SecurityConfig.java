package com.opencourse.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import com.opencourse.course.security.CustomAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain authorisationServerFilterChain(HttpSecurity http)throws Exception{

        http.csrf().disable();
        http.cors().disable();
        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        http.authorizeRequests().mvcMatchers("/api/v1/subscription").permitAll()
        .mvcMatchers("/api/v1/subscription/subscrib").authenticated()

        .mvcMatchers("/api/v1/section/creator").permitAll()
        .mvcMatchers(HttpMethod.GET,"/api/v1/section/**").authenticated()
        .mvcMatchers("/api/v1/section/**").hasAuthority("TEACHER")
      
        .mvcMatchers(HttpMethod.GET,"/api/v1/element/**").authenticated()
        .mvcMatchers("/api/v1/element/**").hasAuthority("TEACHER")
        
        .mvcMatchers(HttpMethod.GET,"/api/v1/topic").permitAll()
        .mvcMatchers("/api/v1/topic").hasAuthority("ADMIN")

        .mvcMatchers(HttpMethod.GET,"/api/v1/course/**").authenticated()
        .mvcMatchers("/api/v1/course/free/**","/api/v1/courrse/paid/**").hasAuthority("ADMIN")
        .mvcMatchers("/api/v1/couse/**").hasAnyAuthority("TEACHER")

        .and().addFilterBefore(filter, AnonymousAuthenticationFilter.class);

        return http.build();
    }

}
