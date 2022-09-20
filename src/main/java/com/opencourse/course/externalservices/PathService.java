package com.opencourse.course.externalservices;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencourse.course.prop.ExternalServicesProp;

@Service
public class PathService {

    private final ExternalServicesProp prop;
    private RestTemplate restTemplate;
    
    public PathService(ExternalServicesProp prop){
        this.prop=prop;
        restTemplate=new RestTemplate();
    }

    public boolean userHasAccessToCourse(Long courseId,Long userId){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String,Object> map=new HashMap<>();
        map.put("courseId",courseId);
        map.put("userId", userId);
        
        HttpEntity<Map<String,Object>> request=new HttpEntity<Map<String,Object>>(map,headers);
        
        ResponseEntity<Boolean> response=restTemplate.postForEntity(prop.getPathAccessUrl(), request, Boolean.class);
        
        return response.getBody();
    }

}
