package com.opencourse.course.externalservices;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencourse.course.prop.ExternalServicesProp;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {
    
    private ExternalServicesProp prop;
    private RestTemplate restTemplate;
    
    public AuthenticationService(ExternalServicesProp prop){
        this.prop=prop;
        restTemplate=new RestTemplate();
    }

    public Boolean isValid(String token){
        ResponseEntity<Boolean> result=restTemplate.postForEntity(prop.getAuthUrl() + "?token=" + token,null, Boolean.class);
        log.warn("token validation returned " + result.getBody());
        return result.getBody();
    }

}
