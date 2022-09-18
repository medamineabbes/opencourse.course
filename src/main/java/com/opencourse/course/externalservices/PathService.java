package com.opencourse.course.externalservices;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.opencourse.course.prop.ExternalServicesProp;

import lombok.Data;

@Service
public class PathService {

    private final ExternalServicesProp prop;
    private RestTemplate restTemplate;
    
    public PathService(ExternalServicesProp prop){
        this.prop=prop;
        restTemplate=new RestTemplate();
    }

    public boolean userHasAccessToCourse(Long courseId,Long userId){
        AccessDto dto=new AccessDto();
        dto.setCourseId(courseId);
        dto.setUserId(userId);
        ResponseEntity<Boolean> response=restTemplate.postForEntity(prop.getPathAccessUrl(), dto, Boolean.class);
        return response.getBody();
    }

}

@Data
class AccessDto{
    private Long courseId;
    private Long userId;
}