package com.opencourse.course.exceptions;


public class TopicNotFoundException extends RuntimeException{
    public TopicNotFoundException(Long id){
        super("topic with id : " + id + " not found");
    }
}
