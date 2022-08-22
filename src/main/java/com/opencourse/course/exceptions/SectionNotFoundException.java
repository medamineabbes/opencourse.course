package com.opencourse.course.exceptions;

public class SectionNotFoundException extends RuntimeException{
    public SectionNotFoundException(Long id){
        super("section with id : " + id + " not found");
    }
}
