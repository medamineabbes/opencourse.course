package com.opencourse.course.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.opencourse.course.dtos.OutlineElementDto;
import com.opencourse.course.entities.Course;
import com.opencourse.course.entities.OutlineElement;
import com.opencourse.course.exceptions.CourseNotFoundException;
import com.opencourse.course.exceptions.OutlineElementNotFoundException;
import com.opencourse.course.exceptions.UnAuthorizedActionException;
import com.opencourse.course.repos.CourseRepo;
import com.opencourse.course.repos.OutlineElementRepo;

public class OutlineElementServiceTest {

    private CourseRepo courseRepo=mock(CourseRepo.class);
    private OutlineElementRepo elementRepo=mock(OutlineElementRepo.class);
    private OutlineElementService service;
    
    private Course c1,c2;
    private OutlineElement e1,e2,e3,e4,e5,e6;


    @BeforeEach
    public void init(){
        service=new OutlineElementService(courseRepo, elementRepo);

        c1=new Course();
        c2=new Course();
        e1=new OutlineElement();
        e2=new OutlineElement();
        e3=new OutlineElement();
        e4=new OutlineElement();
        e5=new OutlineElement();
        e6=new OutlineElement();

        c1.setCreatorId(1L);
        c2.setCreatorId(2L);

        c1.setActive(true);
        c2.setActive(false);

        c1.setElements(List.of(e1,e2,e3));
        c2.setElements(List.of(e4,e5,e6));

        e1.setCourse(c1);
        e2.setCourse(c1);
        e3.setCourse(c1);
        e4.setCourse(c2);
        e5.setCourse(c2);
        e6.setCourse(c2);

    }

    @Test
    @DisplayName("should add Element")
    public void addElementTest(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        OutlineElementDto e=new OutlineElementDto();
        e.setCourseId(1L);
        e.setMarkdownContent("markdownContent");
        e.setTitle("title");
        
        service.addOutlineElement(e, 1L);

        verify(elementRepo).save(any(OutlineElement.class));
    }

    @Test
    @DisplayName("should throw CourseNotFoundException")
    public void addElementErrorTest(){
        OutlineElementDto e=new OutlineElementDto();
        e.setCourseId(1L);
        e.setMarkdownContent("markdownContent");
        e.setTitle("title");
        
        assertThrows(CourseNotFoundException.class, ()->{
            service.addOutlineElement(e, 1L);
        });
    }

    @Test
    @DisplayName("should throws UnAuthorizedActionException")
    public void addElementErrorTest2(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        OutlineElementDto e=new OutlineElementDto();
        e.setCourseId(1L);
        e.setMarkdownContent("markdownContent");
        e.setTitle("title");
        
       assertThrows(UnAuthorizedActionException.class, ()->{
        service.addOutlineElement(e, 2L);
       });

    }

    @Test
    @DisplayName("should throw return List od elements")
    public void getElementsByCourseTest(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        List<OutlineElementDto> elements=service.getOutlineElementByCourse(1L,1L);
        assertEquals(3,elements.size());
    }

    @Test
    @DisplayName("should throw Course Not Found Exception")
    public void getElementsByCourseErrorTest(){
        when(courseRepo.findById(2L)).thenReturn(Optional.of(c2));
        assertThrows(CourseNotFoundException.class,()->{
            service.getOutlineElementByCourse(2L, 1L);
        });
    }

    @Test
    @DisplayName("sould throws CourseNotFoundException")
    public void getElementByCourseErrorTest2(){
        assertThrows(CourseNotFoundException.class,()->{
            service.getOutlineElementByCourse(2L, 1L);
        });
    }

    @Test
    @DisplayName("should return a list of elements")
    public void getElementsByCourseTest3(){
        when(courseRepo.findById(2L)).thenReturn(Optional.of(c2));

        List<OutlineElementDto> elements=service.getOutlineElementByCourse(2L, 2L);

        assertEquals(3, elements.size());
    }

    @Test
    @DisplayName("should return outline element")
    public void getOutlineElementByIdTest(){
        when(elementRepo.findById(1L)).thenReturn(Optional.of(e1));

        OutlineElementDto dto=service.getOutlineElementById(1L, 2L);
        assertEquals(e1.getTitle(), dto.getTitle());
        assertEquals(e1.getMarkdownContent(), dto.getMarkdownContent());
    }

    @Test
    @DisplayName("should return outline element")
    public void getOutlineElementByIdTest2(){
        when(elementRepo.findById(4L)).thenReturn(Optional.of(e4));

        OutlineElementDto dto=service.getOutlineElementById(4L, 2L);
        assertEquals(e2.getTitle(), dto.getTitle());
        assertEquals(e2.getMarkdownContent(), dto.getMarkdownContent());
    }

    @Test
    @DisplayName("should throw elementNotFoundException")
    public void getElementByIdErrorTest(){
        when(elementRepo.findById(2L)).thenReturn(Optional.of(e4));
        assertThrows(OutlineElementNotFoundException.class, ()->{
            service.getOutlineElementById(2L, 1L);
        });
    }

    @Test
    @DisplayName("should throw elementNotFoundException")
    public void getElementByIdErrorTest2(){
        assertThrows(OutlineElementNotFoundException.class, ()->{
            service.getOutlineElementById(2L, 2L);
        });
    }

    @Test
    @DisplayName("should update element")
    public void updateElementTest(){
        OutlineElementDto dto=new OutlineElementDto();
        dto.setId(1L);
        dto.setMarkdownContent("markdownConten t");
        dto.setTitle("titl e ");
        when(elementRepo.findById(1L)).thenReturn(Optional.of(e1));

        service.updateOutlineElement(dto, 1L);

        verify(elementRepo).flush();
    }

    @Test
    @DisplayName("should throw ElementNotFoundException")
    public void updateElementErrorTest(){
        OutlineElementDto dto=new OutlineElementDto();
        dto.setId(1L);
        assertThrows(OutlineElementNotFoundException.class, ()->{
            service.updateOutlineElement(dto, 1L);
        });
    }

    @Test
    @DisplayName("should throw UnAuthActionException")
    public void updateElementErrorTest2(){
        OutlineElementDto dto=new OutlineElementDto();
        dto.setId(1L);
        dto.setMarkdownContent("ma rk do wnContent"); 
        dto.setTitle("ti tle ");
        when(elementRepo.findById(1L)).thenReturn(Optional.of(e1));
        assertThrows(UnAuthorizedActionException.class,()->{
            service.updateOutlineElement(dto, 2L);
        });
    }

    @Test
    @DisplayName("should delete element")
    public void deleteElementTest(){
        when(elementRepo.findById(1L)).thenReturn(Optional.of(e1));
        service.deleteOutlineElementById(1L, 1L);
        verify(elementRepo).deleteById(1L);
    }

    @Test
    @DisplayName("should throw element not Found exception")
    public void deleteElementErrorTest(){
        assertThrows(OutlineElementNotFoundException.class, ()->{
            service.deleteOutlineElementById(1L, 1L);
        });
    }

    @Test
    @DisplayName("should throw Un Auth Action Exception")
    public void deleteElementErrorTest2(){
        when(elementRepo.findById(1L)).thenReturn(Optional.of(e1));
        assertThrows(UnAuthorizedActionException.class, ()->{
            service.deleteOutlineElementById(1L, 2L);
        });
    }
}
