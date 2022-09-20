package com.opencourse.course.exceptions;

public class CustomAuthenticationException extends RuntimeException{
    public CustomAuthenticationException(){
        super("authentication error");
    }
}
