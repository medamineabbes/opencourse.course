package com.opencourse.course.exceptions;

public class FreeCourseSubscriptionException extends RuntimeException{
    public FreeCourseSubscriptionException(Long id){
        super("course with id : " + id + " is free");
    }
}
