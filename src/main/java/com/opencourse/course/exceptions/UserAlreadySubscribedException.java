package com.opencourse.course.exceptions;

public class UserAlreadySubscribedException extends RuntimeException{
    public UserAlreadySubscribedException(Long userId,Long courseId){
        super("user with id : " + userId + " is already subscribed to course with id : " + courseId);
    }
}
