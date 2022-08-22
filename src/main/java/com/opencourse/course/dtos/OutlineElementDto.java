package com.opencourse.course.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.opencourse.course.entities.OutlineElement;

import lombok.Data;

@Data
public class OutlineElementDto {
    private Long id;

    @NotBlank(message = "title is mandatory")
    @NotNull(message = "title is mandatory")
    @Size(max=50,message = "the max number of character in title is 50")
    private String title;
    
    @NotBlank(message = "content is mandatory")
    @NotNull(message = "content is mandatory")
    private String markdownContent;

    private Long CourseId;


    public static OutlineElementDto toDto(OutlineElement element){
        OutlineElementDto outlineElement =new OutlineElementDto();
        outlineElement.setCourseId(element.getCourse().getId());
        outlineElement.setId(element.getId());
        outlineElement.setMarkdownContent(element.getMarkdownContent());
        outlineElement.setTitle(element.getTitle());
        return outlineElement;
    }

    public static OutlineElement toOutlineElement(OutlineElementDto element){
        OutlineElement outlineElement =new OutlineElement();
        outlineElement.setId(element.getId());
        outlineElement.setMarkdownContent(element.getMarkdownContent());
        outlineElement.setTitle(element.getTitle());
        return outlineElement;
    }
}
