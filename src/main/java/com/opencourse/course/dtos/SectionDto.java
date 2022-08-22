package com.opencourse.course.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.opencourse.course.entities.Section;

import lombok.Data;

@Data
public class SectionDto {

    private Long id;
    @NotBlank(message = "title is mandatory")
    @NotNull(message = "title is mandatory")
    private String title;
    private Long courseId;

    public static SectionDto toDto(Section s){
        SectionDto section=new SectionDto();
        section.setCourseId(s.getCourse().getId());
        section.setId(s.getId());
        section.setTitle(s.getTitle());
        return section;
    } 

    public static Section toSection(SectionDto s){
        Section section=new Section();
        section.setId(s.getId());
        section.setTitle(s.getTitle());
        return section;
    }
}
