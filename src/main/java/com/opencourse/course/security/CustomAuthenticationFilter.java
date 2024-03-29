package com.opencourse.course.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.opencourse.course.exceptions.CustomAuthenticationException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends OncePerRequestFilter{

    private final JwtProvider provider;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
        String token=request.getHeader("Authentication");
        if(token!=null){//token exists
            if(provider.isValide(token)){//token is valid

            Authentication auth=provider.getAuth(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("role is " + auth.getAuthorities().toString());
            }else{//token not valid
                throw new CustomAuthenticationException();
            }
            
        }else{//token doe not exists
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        filterChain.doFilter(request, response);    
    }
    
}
