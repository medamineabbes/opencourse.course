package com.opencourse.course.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.opencourse.course.entities.Course;
import com.opencourse.course.entities.CourseSubscription;
import com.opencourse.course.entities.CourseSubscriptionType;
import com.opencourse.course.entities.Section;
import com.opencourse.course.externalservices.PathService;
import com.opencourse.course.repos.CourseRepo;
import com.opencourse.course.repos.CourseSubscriptionRepo;
import com.opencourse.course.repos.CourseSubscriptionTypeRepo;
import com.opencourse.course.repos.SectionRepo;

public class SubscriptionServiceTest {
    private PathService pathService=mock(PathService.class);
    private SectionRepo sectionRepo=mock(SectionRepo.class);
    private CourseSubscriptionRepo subRepo=mock(CourseSubscriptionRepo.class);
    private SubscriptionService service=mock(SubscriptionService.class);
    private CourseRepo courseRepo=mock(CourseRepo.class);
    private CourseSubscriptionTypeRepo subTypeRepo=mock(CourseSubscriptionTypeRepo.class);

    Course c1,c2,c3;
    Section s1,s2,s3;
    CourseSubscription sub1,sub2;
    CourseSubscriptionType t1,t2;
    
    @BeforeEach
    public void init(){
        service=new SubscriptionService(pathService,sectionRepo,subRepo,courseRepo,subTypeRepo);
        c1=new Course();
        c2=new Course();
        c3=new Course();

        s1=new Section();
        s2=new Section();
        s3=new Section();

        t1=new CourseSubscriptionType();
        t2=new CourseSubscriptionType();

        sub2=new CourseSubscription();
        sub1=new CourseSubscription();


        c1.setCreatorId(15L);
        c2.setCreatorId(14L);
        c3.setCreatorId(13L);



        c1.setSections(List.of(s1));
        c2.setSections(List.of(s2));
        c3.setSections(List.of(s3));
        s1.setCourse(c1);
        s2.setCourse(c2);
        s3.setCourse(c3);

        c1.setId(1L);
        c1.setActive(true);
        c1.setFree(false);
        c2.setActive(true);
        c2.setFree(false);
        c2.setId(2L);
        c3.setId(3L);
        c3.setActive(true);
        c3.setFree(true);



        sub1.setType(t1);
        t1.setSubscriptions(List.of(sub1));
        sub2.setType(t2);
        t2.setSubscriptions(List.of(sub2));

        t1.setDurationByMonth((short)1);
        t1.setName("one month");
        t1.setPrice((float)50.0);
        
        t2.setDurationByMonth((short)3);
        t2.setName("three month");
        t2.setPrice((float)120.0);

        sub1.setCourse(c1);
        sub2.setCourse(c2);

        sub1.setUserId(1L);
        sub2.setUserId(2L);
    }

    @Test
    @DisplayName("should have access")
    public void hasAccressTest(){
        sub1.setDate(LocalDateTime.now().minusDays(28));
        
        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));
        when(subRepo.findByCourseIdAndUserId(1L, 1L)).thenReturn(Optional.of(sub1));

        boolean hasAccess=service.userHasAccessToSection(1L, 1L);
        assertTrue(hasAccess);
    }

    @Test
    @DisplayName("should have access because free")
    public void hasAccessTestFree(){
        when(sectionRepo.findById(3L)).thenReturn(Optional.of(s3));
        boolean hasAccess=service.userHasAccessToSection(3L, 1L);
        assertTrue(hasAccess);
    }

    @Test
    @DisplayName("should not have acces because sub expired")
    public void hasAccessExpirationTest(){
        sub1.setDate(LocalDateTime.now().minusDays(32));

        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));
        when(subRepo.findByCourseIdAndUserId(1L, 1L)).thenReturn(Optional.of(sub1));

        boolean hasAccess=service.userHasAccessToSection(1L, 1L);
        assertFalse(hasAccess);
    }

    @Test
    @DisplayName("should not have access because not subscirbed")
    public void hasAccessNotSubTest(){
        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));

        boolean hasAccess=service.userHasAccessToSection(1L, 1L);

        assertFalse(hasAccess);
    }

    @Test
    @DisplayName("should not have access (course is not active)")
    public void hasAccessNotActiveTest(){
        c1.setActive(false);
        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));

        boolean hasAccess=service.userHasAccessToSection(1L, 2L);

        assertFalse(hasAccess);
    }

    @Test
    @DisplayName("should have access (course creator)")
    public void hasAccessNotActiveCreatorTest(){
        c1.setActive(false);
        c1.setCreatorId(2L);
        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));

        boolean hasAccess=service.userHasAccessToSection(1L, 2L);

        assertTrue(hasAccess);
    }

    @Test
    @DisplayName("should have access (path)")
    public void hasAccessPathTest(){
        c1.setActive(true);
        c1.setCreatorId(1L);

        when(sectionRepo.findById(1L)).thenReturn(Optional.of(s1));
        when(pathService.userHasAccessToCourse(c1.getId(), 2L)).thenReturn(true);

        boolean hasAccess=service.userHasAccessToSection(1L, 2L);

        assertTrue(hasAccess);
    }
    
}
