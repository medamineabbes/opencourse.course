package com.opencourse.course.externalservices;

import org.springframework.stereotype.Service;

@Service
public class PathService {
    public boolean userHasAccessToCourse(Long courseId,Long userId){
        return true;
    }
}
