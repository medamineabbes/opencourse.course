package com.opencourse.course.externalservices;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChapterService {
    
    private static String baseUrl="https://opencourse-chapter.herokuapp.com/api/v1/chapter";
    
    public boolean validSections(List<Long> sectionIds){
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<Boolean> result=restTemplate.postForEntity(baseUrl+"/valid", sectionIds, Boolean.class);
        return result.getBody();
    }
}
