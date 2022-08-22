package com.opencourse.course.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.opencourse.course.entities.CourseSubscriptionType;

import lombok.Data;

@Data
public class SubscriptionTypeDto {
    private Long id;

    @NotBlank(message = "name is mandatory")
    @NotNull(message = "name is mandatory")
    private String name;

    @NotBlank(message = "description is mandatory")
    @NotNull(message = "description is mandatory")
    private String description;

    @NotNull(message = "price is mandatory")
    private float price;

    @NotNull(message = "duration  is mandatory")
    private short durationByMonth;

    public static SubscriptionTypeDto toDto(CourseSubscriptionType cst){
        SubscriptionTypeDto subscriptionType=new SubscriptionTypeDto();
        subscriptionType.setDescription(cst.getDescription());
        subscriptionType.setDurationByMonth(cst.getDurationByMonth());
        subscriptionType.setId(cst.getId());
        subscriptionType.setName(cst.getName());
        subscriptionType.setPrice(cst.getPrice());
        return subscriptionType;
    }

    public static CourseSubscriptionType toCourseSubscriptionType(SubscriptionTypeDto cst){
        CourseSubscriptionType subscriptionType=new CourseSubscriptionType();
        subscriptionType.setDescription(cst.getDescription());
        subscriptionType.setDurationByMonth(cst.getDurationByMonth());
        subscriptionType.setId(cst.getId());
        subscriptionType.setName(cst.getName());
        subscriptionType.setPrice(cst.getPrice());
        return subscriptionType;
    }
}
