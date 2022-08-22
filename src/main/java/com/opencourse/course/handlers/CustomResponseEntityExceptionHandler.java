package com.opencourse.course.handlers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.opencourse.course.exceptions.CourseNotFoundException;
import com.opencourse.course.exceptions.CourseNotValidException;
import com.opencourse.course.exceptions.FreeCourseSubscriptionException;
import com.opencourse.course.exceptions.OutlineElementNotFoundException;
import com.opencourse.course.exceptions.PaymentException;
import com.opencourse.course.exceptions.SectionNotFoundException;
import com.opencourse.course.exceptions.SubscriptionTypeNotFoundException;
import com.opencourse.course.exceptions.TopicNotFoundException;
import com.opencourse.course.exceptions.UnAuthorizedActionException;
import com.opencourse.course.exceptions.UserAlreadySubscribedException;

import lombok.Data;

@Data
class ApiError{
    HttpStatus status;
    String message;
    List<String> errors;
}
@RestControllerAdvice
public class CustomResponseEntityExceptionHandler {
    
    @ExceptionHandler({CourseNotFoundException.class})
    public ResponseEntity<Object> handleCourseNotFoundException(CourseNotFoundException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error,error.getStatus());
    }

    @ExceptionHandler({CourseNotValidException.class})
    public ResponseEntity<Object> handleCourseNotValidException(CourseNotValidException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.NO_CONTENT);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error,error.getStatus());
    }

    @ExceptionHandler({FreeCourseSubscriptionException.class})
    public ResponseEntity<Object> handleFreeCourseSubscriptionException(FreeCourseSubscriptionException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.CONFLICT);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error,error.getStatus());
    }

    @ExceptionHandler({OutlineElementNotFoundException.class})
    public ResponseEntity<Object> handleOutlineElementNotFoundException(OutlineElementNotFoundException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error,error.getStatus());
    }

    @ExceptionHandler({SectionNotFoundException.class})
    public ResponseEntity<Object> handleSectionNotFoundException(SectionNotFoundException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error,error.getStatus());
    }

    @ExceptionHandler({SubscriptionTypeNotFoundException.class})
    public ResponseEntity<Object> handleSubscriptionTypeNotFoundException(SubscriptionTypeNotFoundException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error,error.getStatus());
    }

    @ExceptionHandler({TopicNotFoundException.class})
    public ResponseEntity<Object> handleTopicNotFoundException(TopicNotFoundException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error,error.getStatus());
    }


    @ExceptionHandler({UnAuthorizedActionException.class})
    public ResponseEntity<Object> handleUnAuthorizedActionException(UnAuthorizedActionException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.FORBIDDEN);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error,error.getStatus());
    }

    @ExceptionHandler({UserAlreadySubscribedException.class})
    public ResponseEntity<Object> handleUserAlreadySubscribedException(UserAlreadySubscribedException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.CONFLICT);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error,error.getStatus());
    }

    @ExceptionHandler({PaymentException.class})
    public ResponseEntity<Object> handlePaymentException(PaymentException ex,WebRequest request){
        ApiError error=new ApiError();
        error.setStatus(HttpStatus.NOT_IMPLEMENTED);
        error.setMessage(ex.getLocalizedMessage());
        return new ResponseEntity<Object>(error,error.getStatus());
    }
}
