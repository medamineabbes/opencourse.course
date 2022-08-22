package com.opencourse.course.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.opencourse.course.dtos.CourseTopicDto;
import com.opencourse.course.entities.CourseTopic;
import com.opencourse.course.exceptions.TopicNotFoundException;
import com.opencourse.course.repos.CourseTopicRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
class CourseTopicService {
    private final CourseTopicRepo repo;

    //only admin
    public Long addCourseTopic(CourseTopicDto courseTopicDto){
        CourseTopic topic=CourseTopicDto.toCourseTopic(courseTopicDto);
        repo.save(topic);
        return topic.getId();
    }
   
    //every one
    public List<CourseTopicDto> getAll(){
        return repo.findAll()
        .stream()
        .map((courseTopic)->CourseTopicDto.toDto(courseTopic))
        .collect(Collectors.toList());
    }

    //only admin
    public void updateCourseTopic(CourseTopicDto courseTopicDto){
        //make sure course topic exists
        CourseTopic topic=repo
        .findById(courseTopicDto.getId())
        .orElseThrow(()->new TopicNotFoundException(courseTopicDto.getId()));
        
        topic.setName(courseTopicDto.getName());
        repo.flush();        
    }

    
}