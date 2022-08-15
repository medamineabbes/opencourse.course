package com.opencourse.course.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title is mandatory")
    @NotNull(message = "title is mandatory")
    @Size(max=50,message = "the max number of character in title is 50")
    private String title;
    
    @NotNull(message = "average time is mandatory")
    private int avgTimeInHours;
    
    @NotNull(message = "difficulty is mandatory")
    private Difficulty diffiulty;
    
    @NotNull
    private boolean active;
    
    @NotNull(message = "video is mandatory")
    @NotBlank(message = "video is mandatory")
    private String introVideoUrl;
    
    @NotNull
    private Long creatorId;

    private boolean free;

    //sections
    @OneToMany(mappedBy = "course")
    private List<Section> sections;

    //topic
    @ManyToOne
    private CourseTopic topic;

    //outline elements
    @OneToMany(mappedBy = "course")
    private List<OutlineElement> elements;

    //certificates
    @OneToMany(mappedBy = "course")
    private List<Certificate> certificates;

    @OneToMany(mappedBy = "course")
    private List<CourseSubscription> subscriptions;
}
