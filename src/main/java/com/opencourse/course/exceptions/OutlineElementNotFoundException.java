package com.opencourse.course.exceptions;

public class OutlineElementNotFoundException extends RuntimeException{
    public OutlineElementNotFoundException(Long id){
        super("Outline element with id : " + id + " not found");
    }
}
