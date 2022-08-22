package com.opencourse.course.exceptions;

public class UncorrectAmountException  extends RuntimeException{
    public UncorrectAmountException(){
        super("the amount is uncorrect");
    }
}
