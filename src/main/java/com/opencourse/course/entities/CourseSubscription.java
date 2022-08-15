package com.opencourse.course.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class CourseSubscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;
    
    @NotNull
    private LocalDateTime date; 
    
    @NotNull
    private float price;

    //course
    @ManyToOne
    private Course course;

    //payment
    @OneToOne(cascade = CascadeType.ALL)
    private CoursePayment payment;

    //type
    @ManyToOne
    private CourseSubscriptionType type;
}
