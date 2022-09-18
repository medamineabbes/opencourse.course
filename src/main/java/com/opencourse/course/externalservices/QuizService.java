package com.opencourse.course.externalservices;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencourse.course.prop.ExternalServicesProp;

import lombok.Data;

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
    public boolean validSections(List<Long> sectionIds){
        ResponseEntity<Boolean> result=restTemplate.postForEntity(prop.getQuizValidationUrl(), sectionIds, Boolean.class);
        return result.getBody();
    }

    //test if user finished all quizs
    //default to true
    public boolean finishedSections(List<Long> sectionIds,Long userId){
        VerifyQuizDto vqDto=new VerifyQuizDto();
        vqDto.setSectionIds(sectionIds);
        vqDto.setUserId(userId);
        ResponseEntity<Boolean> result=restTemplate.postForEntity(prop.getQuizFinishedUrl(), vqDto, Boolean.class);
        return result.getBody();
    }

}

@Data
class VerifyQuizDto{
    private List<Long> sectionIds;
    private Long userId;
}