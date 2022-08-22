package com.opencourse.course.apis;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencourse.course.dtos.CourseTopicDto;
import com.opencourse.course.services.CourseTopicService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/course/topic")
@AllArgsConstructor
public class CourseTopicController {
    
    private final CourseTopicService service;

    //all users
    @GetMapping
    public ResponseEntity<List<CourseTopicDto>> getAllTopics(){
        return ResponseEntity.ok(service.getAll());
    }

    //only admin
    @PostMapping
    public ResponseEntity<Long> addCourseTopic(@RequestBody(required = true) CourseTopicDto courseTopicDto){
        return ResponseEntity.ok(service.addCourseTopic(courseTopicDto));
    }

    //only admin
    @PutMapping
    public ResponseEntity<Object> updateCourseTopic(@RequestBody(required = true) CourseTopicDto courseTopicDto){
        service.updateCourseTopic(courseTopicDto);
        return ResponseEntity.ok().build();
    }

}
