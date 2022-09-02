package com.opencourse.course.api;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static  org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencourse.course.apis.SectionController;
import com.opencourse.course.services.SectionService;

@WebMvcTest(SectionController.class)
public class sectionControllerTest {
    @MockBean
    SectionService service;
    private static String baseUrl="/api/v1/course/section"; 
    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("should return 200 status code")
    public void userCretedSectionTest() throws JsonProcessingException, Exception{    
        Long userId=15L;
        Long sectionId=1L;
        when(service.userCreatedSection(sectionId,userId)).thenReturn(true);
        ObjectMapper mapper=new ObjectMapper();
        
        Map<String,Object> body=new HashMap<>();
        body.put("userId",userId);
        body.put("sectionId",sectionId);

        mvc.perform(
            post(baseUrl)
            .content(mapper.writeValueAsBytes(body))
            .contentType(MediaType.APPLICATION_JSON)    
        ).andExpect(status().isOk());
        
    }
}
