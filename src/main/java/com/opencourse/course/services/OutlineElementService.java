package com.opencourse.course.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.opencourse.course.dtos.OutlineElementDto;
import com.opencourse.course.entities.Course;
import com.opencourse.course.entities.OutlineElement;
import com.opencourse.course.exceptions.CourseNotFoundException;
import com.opencourse.course.exceptions.OutlineElementNotFoundException;
import com.opencourse.course.exceptions.UnAuthorizedActionException;
import com.opencourse.course.repos.CourseRepo;
import com.opencourse.course.repos.OutlineElementRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OutlineElementService {
    
    private final CourseRepo courseRepo;
    private final OutlineElementRepo elementRepo;


    //only teacher
    public Long addOutlineElement(OutlineElementDto outlineElementDto,Long userId){
        //make sure course exists
        Course course=courseRepo.findById(outlineElementDto.getCourseId())
        .orElseThrow(()->new CourseNotFoundException(outlineElementDto.getCourseId()));
        
        //make sure user is creator 
        if(!course.getCreatorId().equals(userId))
        throw new UnAuthorizedActionException();

        //add element
        OutlineElement element=OutlineElementDto.toOutlineElement(outlineElementDto);
        element.setCourse(course);
        elementRepo.save(element);
        return element.getId();
    }

    //every one
    //getByCourse only active courses (if not active only creator can get)
    public List<OutlineElementDto> getOutlineElementByCourse(Long courseId,Long userId){
        //make sure course exists
        Course course=courseRepo.findById(courseId)
        .orElseThrow(()->new CourseNotFoundException(courseId));

        //only active courses for users
        //creator can see all
        if(!course.isActive() && !course.getCreatorId().equals(userId))
        throw new CourseNotFoundException(courseId);

        return course
        .getElements()
        .stream()
        .map((element)->OutlineElementDto.toDto(element))
        .collect(Collectors.toList());
    }

    //every one
    //get element only active courses (if not active only creator can get)
    public OutlineElementDto getOutlineElementById(Long id,Long userId){
        //make sure element exists
        OutlineElement element=elementRepo
        .findById(id)
        .orElseThrow(()->new OutlineElementNotFoundException(id));

        Course course=element.getCourse();

        //only creator can see inactive courses
        if(!course.isActive() && !course.getCreatorId().equals(userId))
        throw new OutlineElementNotFoundException(id);

        return OutlineElementDto.toDto(element);
    }
    

    //only teacher
    public void updateOutlineElement(OutlineElementDto outlineElementDto,Long userId){
        //make sure element exists
        OutlineElement element=elementRepo
        .findById(outlineElementDto.getId())
        .orElseThrow(()->new OutlineElementNotFoundException(outlineElementDto.getId()));

        //only creator can update elements
        if(!element.getCourse().getCreatorId().equals(userId))
        throw new UnAuthorizedActionException();

        element.setMarkdownContent(outlineElementDto.getMarkdownContent());
        element.setTitle(outlineElementDto.getTitle());
        elementRepo.flush();
    }

    //only teacher
    //delete an element
    public void deleteOutlineElementById(Long id,Long userId){
        //make sure element exists
        OutlineElement element=elementRepo.findById(id)
        .orElseThrow(()->new OutlineElementNotFoundException(id));

        //only creator can delete elements
        if(!element.getCourse().getCreatorId().equals(userId))
        throw new UnAuthorizedActionException();
        elementRepo.deleteById(id);
    }
    
}
