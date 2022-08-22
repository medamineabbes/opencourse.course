package com.opencourse.course.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.opencourse.course.entities.CourseTopic;

import lombok.Data;
@Data
public class CourseTopicDto {
    private Long id;

    @NotBlank(message = "name is mandatory")
    @NotNull(message = "name is mandatory")
    private String name;


    public static CourseTopicDto toDto(CourseTopic topic){
        CourseTopicDto courseTopic=new CourseTopicDto();
        courseTopic.setId(topic.getId());
        courseTopic.setName(topic.getName());
        return courseTopic;
    }

    public static CourseTopic toCourseTopic(CourseTopicDto topic){
        CourseTopic courseTopic=new CourseTopic();
        courseTopic.setId(topic.getId());
        courseTopic.setName(topic.getName());
        return courseTopic;
    }
    
}
