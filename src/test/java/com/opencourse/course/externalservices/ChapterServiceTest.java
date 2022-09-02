package com.opencourse.course.externalservices;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChapterServiceTest {
    private ChapterService chapterService;

    @BeforeEach
    public void init(){
        chapterService=new ChapterService();
    }

    @Test
    @DisplayName("should return false")
    public void validSectionsTest(){
        Boolean response=chapterService.validSections(List.of(1L,2L));

        assertFalse(response);

    }

}
