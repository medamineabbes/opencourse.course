package com.opencourse.course.externalservices;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.opencourse.course.prop.ExternalServicesProp;

@SpringBootTest
public class QuizServiceTest {
    
    private QuizService quizService;
    private ExternalServicesProp prop=mock(ExternalServicesProp.class);
    @BeforeEach
    public void init(){
        quizService=new QuizService(prop);
    }

    @Test
    @DisplayName("should return false")
    public void validQuizSectionsTest(){
        List<Long> sectionIds=List.of(1L,2L);
        Boolean result=quizService.validSections(sectionIds);

        assertFalse(result);

    }

    @Test
    @DisplayName("should return false")
    public void finishedSectionsTest(){
        List<Long> sectionIds=List.of(1L,2L);
        Long userId=1L;
        Boolean result=quizService.finishedSections(sectionIds, userId);

        assertFalse(result);
    }


}
