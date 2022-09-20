package com.opencourse.course.externalservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencourse.course.prop.ExternalServicesProp;

@Service
public class QuizService {

    private RestTemplate restTemplate;
    private final ExternalServicesProp prop;

    public QuizService(ExternalServicesProp prop){
        restTemplate=new RestTemplate();
        this.prop=prop;
    }
    
    //test if quizs are valid
    //default to true
    //fix parameters 
    public boolean validSections(List<Long> sectionIds){
        ResponseEntity<Boolean> result=restTemplate.postForEntity(prop.getQuizValidationUrl(), sectionIds, Boolean.class);
        return result.getBody();
    }

    //test if user finished all quizs
    //default to true
    public boolean finishedSections(List<Long> sectionIds,Long userId){
        Map<String,Object> map=new HashMap<>();
        map.put("sectionIds",sectionIds);
        map.put("userId",userId);


        HttpHeaders headers =new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String,Object>> request=new HttpEntity<Map<String,Object>>(map,headers);
        ResponseEntity<Boolean> result=restTemplate.postForEntity(prop.getQuizFinishedUrl(), request, Boolean.class);
        return result.getBody();
    }

}
