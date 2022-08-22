package com.opencourse.course.config;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfig {
    

    @Bean 
    public ClassLoaderTemplateResolver certififcateTemplateResolver(){
        ClassLoaderTemplateResolver pdfTemplateResolver=new ClassLoaderTemplateResolver();
        pdfTemplateResolver.setSuffix(".html");
        pdfTemplateResolver.setPrefix("templates/");
        pdfTemplateResolver.setCSSTemplateModePatterns(Set.of("/static"));
        pdfTemplateResolver.setTemplateMode(TemplateMode.HTML);
        pdfTemplateResolver.setCharacterEncoding("UTF-8");
        pdfTemplateResolver.setOrder(1);
        return pdfTemplateResolver;
    }
}
