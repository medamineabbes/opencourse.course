package com.opencourse.course.exceptions;

public class CourseNotValidException extends RuntimeException{
    public CourseNotValidException(Long id){
        super("course with id : " + id + " is not valid");
    }
}
