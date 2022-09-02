package com.opencourse.course.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.opencourse.course.dtos.SectionDto;
import com.opencourse.course.entities.Course;
import com.opencourse.course.entities.Section;
import com.opencourse.course.exceptions.CourseNotFoundException;
import com.opencourse.course.exceptions.SectionNotFoundException;
import com.opencourse.course.exceptions.UnAuthorizedActionException;
import com.opencourse.course.repos.CourseRepo;
import com.opencourse.course.repos.SectionRepo;

public class SectionSeviceTest {
    private SectionRepo sectionRepo=mock(SectionRepo.class);
    private CourseRepo courseRepo=mock(CourseRepo.class);
    private SectionService service;

    private Course c1,c2;
    private Section s1,s2,s3,s4,s5;
    @BeforeEach
    public void init(){
        service=new SectionService(sectionRepo,courseRepo);

        c1=new Course();
        c2=new Course();
        
        s1=new Section();
        s2=new Section();
        s3=new Section();
        s4=new Section();
        s5=new Section();

        c1.setId(1L);
        c2.setId(2L);
        
        c1.setActive(true);
        c2.setActive(false);
        
        c1.setCreatorId(1L);
        c2.setCreatorId(2L);

        s1.setCourse(c1);
        s2.setCourse(c1);
        s3.setCourse(c2);
        s4.setCourse(c2);
        s5.setCourse(c2);

        c1.setSections(List.of(s1,s2));
        c2.setSections(List.of(s3,s4,s5));

        s1.setId(1L);
        s2.setId(2L);
        s3.setId(3L);
        s4.setId(4L);
        s5.setId(5L);

    }

    @Test
    @DisplayName("should add section")
    public void addSectionTest(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        SectionDto dto=new SectionDto();
        dto.setCourseId(1L);
        dto.setTitle("title");
        service.addSection(dto, 1L);//creator id 1L
        verify(sectionRepo).save(any(Section.class));
    }

    @Test
    @DisplayName("should throw UnAuthActionException")
    public void addSectionErrorTest(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        SectionDto dto=new SectionDto();
        dto.setCourseId(1L);
        dto.setTitle("title");
        assertThrows(UnAuthorizedActionException.class, ()->{
            service.addSection(dto, 2L);//creator id != 2L
        });
    }

    @Test
    @DisplayName("should throw CourseNotFoundException")
    public void addCourseErrorTest2(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        SectionDto dto=new SectionDto();
        dto.setCourseId(1L);
        dto.setTitle("title");
        assertThrows(UnAuthorizedActionException.class, ()->{
            service.addSection(dto, 2L);
        });
    }

    @Test
    @DisplayName("should return a list of sections")
    public void getSectionByCourseTest(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        List<SectionDto> sections=service.getSectionByCourse(1L, 2L);
        assertEquals(2, sections.size());
    }
    
    @Test
    @DisplayName("should return a list of sections")
    public void getSectionByCourseTest2(){
        when(courseRepo.findById(2L)).thenReturn(Optional.of(c2));
        List<SectionDto> sections=service.getSectionByCourse(2L,2L);
        assertEquals(3, sections.size());
    }

    @Test
    @DisplayName("should throw CourseNotFoundException")
    public void getSectionByCourseErrorTest(){
        when(courseRepo.findById(2L)).thenReturn(Optional.of(c2));
        assertThrows(CourseNotFoundException.class,()->{
            service.getSectionByCourse(2L, 1L);
        });
    }

    @Test
    @DisplayName("should throw CourseNotFoundException")
    public void getSectionByCourseErrorTest2(){
        assertThrows(CourseNotFoundException.class, ()->{
            service.getSectionByCourse(1L, 1L);
        });
    }

    @Test
    @DisplayName("should return section")
    public void getSectionByIdTest(){
        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));
        SectionDto section=service.getSectionById(1L, 2L);
        assertEquals(s1.getTitle(), section.getTitle());
    }

    @Test
    @DisplayName("should throw SectionNotFoundException")
    public void getSectionByIdErrorTest(){
        assertThrows(SectionNotFoundException.class, ()->{
            service.getSectionById(15L, 1L);
        });
    }

    @Test
    @DisplayName("should throw SectionNotFoundException")
    public void getSectionByIdErrorTest2(){
        when(sectionRepo.findById(3L)).thenReturn(Optional.of(s3));
        assertThrows(SectionNotFoundException.class, ()->{
            service.getSectionById(3L, 1L);//not creator of the course
        });
    }


    @Test
    @DisplayName("should update section")
    public void updateSectionTest(){
        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));
        SectionDto dto=new SectionDto();
        dto.setId(1L);
        dto.setTitle("title up");
        service.updateSection(dto, 1L);//creator 
        verify(sectionRepo).flush();
    }

    @Test
    @DisplayName("should throw SectionNotFoundException")
    public void updateSectionErrorTest(){
        SectionDto dto=new SectionDto();
        dto.setId(1L);
        dto.setTitle("title up");
        assertThrows(SectionNotFoundException.class, ()->{
            service.updateSection(dto, 1L); 
        });
    }

    @Test
    @DisplayName("should throw UnAuthorisedActionAxception")
    public void updateSectionErrorTest2(){
        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));
        SectionDto dto=new SectionDto();
        dto.setId(1L);
        dto.setTitle("title up");
        assertThrows(UnAuthorizedActionException.class, ()->{
            service.updateSection(dto, 2L);//not creator 
        });
    }


    @Test
    @DisplayName("should return true")
    public void userCretedSectionTest(){
        c1.setCreatorId(15L);
        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));

        Boolean created = service.userCreatedSection(1L, 15L);
        assertTrue(created);
    }

    @Test
    @DisplayName("should return false")
    public void userCretedSectionTest2(){
        c1.setCreatorId(14L);
        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));

        Boolean created = service.userCreatedSection(1L, 15L);
        assertFalse(created);
    }
    
}
