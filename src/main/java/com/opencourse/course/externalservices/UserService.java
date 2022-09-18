package com.opencourse.course.externalservices;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencourse.course.dtos.UserInfoDto;
import com.opencourse.course.prop.ExternalServicesProp;


@Service
public class UserService {

    private RestTemplate restTemplate;
    private final ExternalServicesProp prop;
    
    public UserService(ExternalServicesProp prop){
        this.prop=prop;
        restTemplate=new RestTemplate();
    }

    public  UserInfoDto getUserInfo(Long userId){
        
        ResponseEntity<UserInfoDto> response=restTemplate.getForEntity(prop.getUserInfoUrl() + "/" + userId, UserInfoDto.class);
        
        return response.getBody();
    }

    public Boolean validateToken(String token){
        ResponseEntity<Boolean> response=restTemplate.postForEntity(prop.getAuthUrl(), token, Boolean.class);
        return response.getBody();
    }
    
}
