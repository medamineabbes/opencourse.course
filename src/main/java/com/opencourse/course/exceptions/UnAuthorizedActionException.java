package com.opencourse.course.exceptions;


public class UnAuthorizedActionException extends RuntimeException {
    public UnAuthorizedActionException(){
        super("unauthorized action");
    }
}
