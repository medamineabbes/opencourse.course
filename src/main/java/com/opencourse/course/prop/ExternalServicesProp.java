package com.opencourse.course.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "external")
@Data
public class ExternalServicesProp {
    
    private String quizValidationUrl;
    private String quizFinishedUrl;
    private String validChapterUrl;
    private String pathAccessUrl;
    private String userInfoUrl;
    private String authUrl; 

}
