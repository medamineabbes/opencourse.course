package com.opencourse.course.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.opencourse.course.dtos.SectionDto;
import com.opencourse.course.entities.Course;
import com.opencourse.course.entities.Section;
import com.opencourse.course.exceptions.CourseNotFoundException;
import com.opencourse.course.exceptions.SectionNotFoundException;
import com.opencourse.course.exceptions.UnAuthorizedActionException;
import com.opencourse.course.repos.CourseRepo;
import com.opencourse.course.repos.SectionRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SectionService {
    
    private SectionRepo sectionRepo;
    private CourseRepo courseRepo;
    //CRUD SECTION
    //only course creator can add section
    public Long addSection(SectionDto sectionDto,Long userId){
        //make sure course exists
        Course course=courseRepo
        .findById(sectionDto.getCourseId())
        .orElseThrow(()->new CourseNotFoundException(sectionDto.getCourseId()));
        
        //make sure user created the course
        if(!course.getCreatorId().equals(userId))
        throw new UnAuthorizedActionException();

        Section section=SectionDto.toSection(sectionDto);
        section.setCourse(course);
        sectionRepo.save(section);
        return section.getId();
    }

    //if course is not active verify user is creator 
    public List<SectionDto> getSectionByCourse(Long courseId,Long userId){

        //make sure course exists
        Course course=courseRepo.findById(courseId)
        .orElseThrow(()->new CourseNotFoundException(courseId));

        //make sure course is active or user is creator
        if(!course.isActive() && !course.getCreatorId().equals(userId))
        throw new CourseNotFoundException(courseId);

        return course.getSections()
        .stream().map((section)->SectionDto.toDto(section))
        .collect(Collectors.toList());
    }
    
    //if course is not active verify user is creator 
    public SectionDto getSectionById(Long id,Long userId){
        //make sure section exists
        Section section=sectionRepo.findById(id)
        .orElseThrow(()->new SectionNotFoundException(id));

        Course course=section.getCourse();
        //if course is not active and user is not creator
        if(!course.isActive() && !course.getCreatorId().equals(userId))
        throw new SectionNotFoundException(id);

        return SectionDto.toDto(section);
    }

    //only course creator
    public void updateSection(SectionDto sectionDto,Long userId){
        
        //make sure section exists
        Section section=sectionRepo.findById(sectionDto.getId())
        .orElseThrow(()->new SectionNotFoundException(sectionDto.getId()));

        //only course creator can update details about a course
        Course course =section.getCourse();
        if(!course.getCreatorId().equals(userId))
        throw new UnAuthorizedActionException();

        section.setTitle(sectionDto.getTitle());
        sectionRepo.flush();

    }

    // only from other services
    public Boolean userCreatedSection(Long sectionId,Long userId){
        
        Section section=sectionRepo.findById(sectionId)
        .orElseThrow(()->new SectionNotFoundException(sectionId));

        Course course=section.getCourse();

        return course.getCreatorId().equals(userId);
    }

}
