package com.opencourse.course.services;

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

import com.opencourse.course.dtos.CourseDto;
import com.opencourse.course.entities.Certificate;
import com.opencourse.course.entities.Course;
import com.opencourse.course.entities.CourseSubscription;
import com.opencourse.course.entities.CourseTopic;
import com.opencourse.course.entities.Difficulty;
import com.opencourse.course.entities.Section;
import com.opencourse.course.exceptions.CourseNotFoundException;
import com.opencourse.course.exceptions.CourseNotValidException;
import com.opencourse.course.exceptions.TopicNotFoundException;
import com.opencourse.course.exceptions.UnAuthorizedActionException;
import com.opencourse.course.externalservices.ChapterService;
import com.opencourse.course.externalservices.QuizService;
import com.opencourse.course.externalservices.UserService;
import com.opencourse.course.repos.CertificateRepo;
import com.opencourse.course.repos.CourseRepo;
import com.opencourse.course.repos.CourseTopicRepo;

public class CourseServiceTest {
    
    private CourseRepo courseRepo=mock(CourseRepo.class);
    private CourseTopicRepo courseTopicRepo=mock(CourseTopicRepo.class);
    private CertificateRepo certificateRepo=mock(CertificateRepo.class);
    private ChapterService chapterService=mock(ChapterService.class);
    private QuizService quizService=mock(QuizService.class);
    private CertificateGenerationService certificateService=mock(CertificateGenerationService.class);
    private UserService userService=mock(UserService.class);
    private CourseService service;

    private Course c1,c2,c3;
    private CourseTopic t;
    private Certificate cert;
    private CourseSubscription sub1,sub2;
    @BeforeEach
    public void init(){
        service=new CourseService(courseRepo,
        courseTopicRepo,
        certificateRepo,
        chapterService,
        quizService,
        certificateService,
        userService);

        c1=new Course();
        c2=new Course();
        c3=new Course();

        c1.setActive(true);
        c2.setActive(true);
        c3.setActive(false);

        c1.setCreatorId(1L);
        c2.setCreatorId(2L);
        c3.setCreatorId(3L);

        c1.setDiffiulty(Difficulty.EASY);
        c2.setDiffiulty(Difficulty.EASY);
        c3.setDiffiulty(Difficulty.EASY);
        t=new CourseTopic();
        cert=new Certificate();
        sub1=new CourseSubscription();
        sub2=new CourseSubscription();

        c1.setSubscriptions(List.of(sub1));
        sub1.setCourse(c1);
        c1.setCertificates(List.of(cert));
        cert.setCourse(c1);
        t.setCourses(List.of(c1,c2,c3));
        c1.setTopic(t);
        c3.setTopic(t);


        c2.setTopic(t);
        c2.setSubscriptions(List.of(sub2));
        sub2.setCourse(c2);

        Section s1=new Section();
        Section s2=new Section();
        
        s1.setId(1L);
        s2.setId(2L);
        
        s1.setCourse(c1);
        s2.setCourse(c1);

        s1.setTitle("title s1");
        s2.setTitle("title s2");

        c1.setSections(List.of(s1,s2));
    }

    @Test
    @DisplayName("should add course")
    public void addcourseTest(){
        CourseDto dto=new CourseDto();
        dto.setTopicId(1L);
        dto.setActive(true);
        dto.setDiffiulty("EASY");
        when(courseTopicRepo.findById(1L)).thenReturn(Optional.of(t));

        service.addCourse(dto, 1L);
        verify(courseRepo).save(any(Course.class));
    }
    
    @Test
    @DisplayName("should throw error")
    public void addCourseErrorTest(){
        CourseDto dto=new CourseDto();
        dto.setTopicId(1L);
        dto.setActive(true);
        dto.setDiffiulty("EASY");
        assertThrows(TopicNotFoundException.class, ()->{
            service.addCourse(dto, 1L);
        });
    }
    
    @Test
    @DisplayName("should return course whith finished true")
    public void getCourseByIdAsClientTest(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(certificateRepo.findByCourseIdAndUserId(1L, 15L)).thenReturn(Optional.of(cert));
        CourseDto found=service.getCourseByIdAsClient(1L, 15L);

        assertTrue(found.isFinished());
    }

    @Test
    @DisplayName("should throw exception for the normal user and return unactive course for the creator")
    public void getCourseByIdAsClientTest2(){
        when(courseRepo.findById(3L)).thenReturn(Optional.of(c3));

        assertThrows(CourseNotFoundException.class, ()->{
            service.getCourseByIdAsClient(3L, 15L);
        });

        CourseDto found=service.getCourseByIdAsClient(3L, 3L);
        assertFalse(found.isActive());

    }

    @Test
    @DisplayName("should update course")
    public void updateCourseTest(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(courseTopicRepo.findById(1L)).thenReturn(Optional.of(t));
        CourseDto dto=new CourseDto();
        dto.setId(1L);
        dto.setTopicId(1L);
        dto.setDiffiulty("HARD");
        service.updateCourse(dto, 1L);

        verify(courseRepo).flush();
    }

    @Test
    @DisplayName("should throw UnAuthorizedActionException ")
    public void updateCourseTest2(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(courseTopicRepo.findById(1L)).thenReturn(Optional.of(t));
        CourseDto dto=new CourseDto();
        dto.setId(1L);
        dto.setTopicId(1L);
        assertThrows(UnAuthorizedActionException.class,()->{
            service.updateCourse(dto, 2L);
        });
    }

    @Test
    @DisplayName("should set course as paid")
    public void setCourseAsPaidTest(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));

        service.setCourseAsPaid(1L);
        
        verify(courseRepo).flush();
    }
   
    @Test
    @DisplayName("should throw CourseNotFoundException")
    public void setCourseAsPaidTest2(){

        assertThrows(CourseNotFoundException.class, ()->{
            service.setCourseAsPaid(1L);
        });
    }
    
    @Test
    @DisplayName("should set course as free")
    public void setCourseAsFreeTest(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));

        service.setCourseAsFree(1L);
        
        verify(courseRepo).flush();
    }

    @Test
    @DisplayName("should throw CourseNotFoundException")
    public void setCourseAsFree2(){
        assertThrows(CourseNotFoundException.class, ()->{
            service.setCourseAsPaid(1L);
        });
    }

    @Test
    @DisplayName("should activate course")
    public void activateCourseTest(){

        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(chapterService.validSections(List.of(1L,2L))).thenReturn(true);
        when(quizService.validSections(List.of(1L,2L))).thenReturn(true);

        service.activateCourse(1L, 1L);

        verify(courseRepo).flush();
    }

    @Test
    @DisplayName("should throws CourseNotFoundException")
    public void activateCourseErrorTest(){
        assertThrows(CourseNotFoundException.class, ()->{
            service.activateCourse(1L, 1L);
        });
    }

    @Test
    @DisplayName("should throws UnAuthorizedActionException")
    public void activateCourseErrorTest2(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
       assertThrows(UnAuthorizedActionException.class,()->{
        service.activateCourse(1L, 2L);
       });
    }
    
    @Test
    @DisplayName("should throw CourseNotValidException")
    public void activateCourseErrorTest3(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(chapterService.validSections(List.of(1L,2L))).thenReturn(false);
        assertThrows(CourseNotValidException.class, ()->{
            service.activateCourse(1L, 1L);
        });
    }

    @Test
    @DisplayName("should throw CourseNotValidException")
    public void activateCourseErrorTest4(){
        when(courseRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(chapterService.validSections(List.of(1L,2L))).thenReturn(true);
        when(quizService.validSections(List.of(1L,2L))).thenReturn(false);
        assertThrows(CourseNotValidException.class, ()->{
            service.activateCourse(1L, 1L);
        });
    }


}
