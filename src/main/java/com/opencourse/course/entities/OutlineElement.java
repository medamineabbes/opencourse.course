package com.opencourse.course.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
public class OutlineElement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "title is mandatory")
    @NotNull(message = "title is mandatory")
    @Size(max=50,message = "the max number of character in title is 50")
    private String title;
    
    @NotBlank(message = "content is mandatory")
    @NotNull(message = "content is mandatory")
    private String markdownContent;

    @ManyToOne
    private Course course;

    
}
