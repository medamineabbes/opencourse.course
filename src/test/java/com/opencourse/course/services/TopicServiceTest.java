package com.opencourse.course.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.opencourse.course.dtos.CourseTopicDto;
import com.opencourse.course.entities.CourseTopic;
import com.opencourse.course.exceptions.TopicNotFoundException;
import com.opencourse.course.repos.CourseTopicRepo;

public class TopicServiceTest {
    
    private CourseTopicRepo repo=mock(CourseTopicRepo.class);
    private CourseTopicService service;

    @BeforeEach
    public void init(){
        service=new CourseTopicService(repo);
    }

    @Test
    @DisplayName("should add CourseTopic")
    public void addTopicTes(){
        CourseTopicDto dto=new CourseTopicDto();
        dto.setName("bla bla");
        service.addCourseTopic(dto);
        verify(repo).save(any(CourseTopic.class));
    }

    @Test
    @DisplayName("should return all topics")
    public void getAllTopicTest(){
        CourseTopic topic1=new CourseTopic();
        CourseTopic topic2=new CourseTopic();
        topic1.setId(1L);
        topic2.setId(2l);
        topic1.setName("name 1");
        topic2.setName("name 2");

        when(repo.findAll()).thenReturn(List.of(topic1,topic2));

        List<CourseTopicDto> topics=service.getAll();

        assertEquals(topics.size(), 2);
        assertEquals(topics.get(0).getId(), 1L);
        assertEquals(topics.get(1).getId(), 2L);
    }

    @Test
    @DisplayName("should update course topic")
    public void updateTopicTest(){
        CourseTopic topic=new CourseTopic();
        topic.setId(1L);
        topic.setName("name");
        when(repo.findById(1L)).thenReturn(Optional.of(topic));

        CourseTopicDto dto=new CourseTopicDto();
        dto.setId(1L);
        dto.setName("nameupdatded");
        service.updateCourseTopic(dto);

        verify(repo).flush();
    }
    @Test
    @DisplayName("should throw exception")
    public void updateTopicTestError(){
        CourseTopic topic=new CourseTopic();
        topic.setId(1L);
        topic.setName("name");
        when(repo.findById(1L)).thenReturn(Optional.of(topic));

        CourseTopicDto dto=new CourseTopicDto();
        dto.setId(2L);
        dto.setName("nameupdatded");
        assertThrows(TopicNotFoundException.class, ()->{
            service.updateCourseTopic(dto);
        });
    }
}
