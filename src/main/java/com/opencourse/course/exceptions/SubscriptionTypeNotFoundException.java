package com.opencourse.course.exceptions;

public class SubscriptionTypeNotFoundException extends RuntimeException{
    public SubscriptionTypeNotFoundException(Long id){
        super("subscription type with id : " + id + " not found");
    }
}
