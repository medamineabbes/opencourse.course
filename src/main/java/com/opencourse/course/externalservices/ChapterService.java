package com.opencourse.course.externalservices;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencourse.course.prop.ExternalServicesProp;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChapterService {
    
    private final ExternalServicesProp prop;
    
    public boolean validSections(List<Long> sectionIds){
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<Boolean> result=restTemplate.postForEntity(prop.getValidChapterUrl(), sectionIds, Boolean.class);
        return result.getBody();
    }
    
}
