package com.opencourse.course.dtos;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.opencourse.course.entities.Course;
import com.opencourse.course.entities.Difficulty;

import lombok.Data;

@Data
public class CourseDto {
    private Long id;

    @NotBlank(message = "title is mandatory")
    @NotNull(message = "title is mandatory")
    @Size(max=50,message = "the max number of character in title is 50")
    private String title;
    
    @NotNull(message = "image is mandatory")
    @NotBlank(message = "image is mandatory")
    private String imageUrl;

    @NotNull(message = "average time is mandatory")
    private int avgTimeInHours;
    
    @NotNull(message = "difficulty is mandatory")
    private String diffiulty;
    
    @NotNull
    private boolean active;
    
    @NotNull(message = "video is mandatory")
    @NotBlank(message = "video is mandatory")
    private String introVideoUrl;
    
    @NotNull
    private Long creatorId;

    private boolean free;

    @NotNull(message = "topic is mandatory")
    private Long topicId;


    private boolean finished;

    public static CourseDto toDto(Course c){
        CourseDto course=new CourseDto();
        course.setActive(c.isActive());
        course.setAvgTimeInHours(c.getAvgTimeInHours());
        course.setCreatorId(c.getCreatorId());
        course.setDiffiulty(c.getDiffiulty().toString());
        course.setFree(c.isFree());
        course.setId(c.getId());
        course.setImageUrl(c.getImageUrl());
        course.setIntroVideoUrl(c.getIntroVideoUrl());
        course.setTitle(c.getTitle());
        course.setTopicId(c.getTopic().getId());
        return course;
    }

    public static Course toCourse(CourseDto c){
        Course course=new Course();
        course.setActive(c.isActive());
        course.setAvgTimeInHours(c.getAvgTimeInHours());
        course.setCreatorId(c.getCreatorId());

        // add exception handling for difficulty string value
        course.setDiffiulty(Difficulty.valueOf(c.getDiffiulty()));
        course.setFree(c.isFree());
        course.setId(c.getId());
        course.setImageUrl(c.getImageUrl());
        course.setIntroVideoUrl(c.getIntroVideoUrl());
        course.setTitle(c.getTitle());
        return course;
    }

}
