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
public class ChapterServiceTest {
    private ChapterService chapterService;
    private ExternalServicesProp prop=mock(ExternalServicesProp.class);

    @BeforeEach
    public void init(){
        chapterService=new ChapterService(prop);
    }

    @Test
    @DisplayName("should return false")
    public void validSectionsTest(){
        Boolean response=chapterService.validSections(List.of(1L,2L));

        assertFalse(response);

    }

}
