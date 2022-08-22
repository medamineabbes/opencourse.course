package com.opencourse.course.exceptions;

public class PaymentException extends RuntimeException{
    public PaymentException(){
        super("something went wrong with payment");
    }
}
