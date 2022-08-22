package com.opencourse.course.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.opencourse.course.dtos.ChargeRequestDto;
import com.opencourse.course.entities.Course;
import com.opencourse.course.entities.CourseSubscription;
import com.opencourse.course.entities.CourseSubscriptionType;
import com.opencourse.course.entities.Section;
import com.opencourse.course.exceptions.CourseNotFoundException;
import com.opencourse.course.exceptions.FreeCourseSubscriptionException;
import com.opencourse.course.exceptions.PaymentException;
import com.opencourse.course.exceptions.SectionNotFoundException;
import com.opencourse.course.exceptions.SubscriptionTypeNotFoundException;
import com.opencourse.course.exceptions.UncorrectAmountException;
import com.opencourse.course.exceptions.UserAlreadySubscribedException;
import com.opencourse.course.externalservices.PathService;
import com.opencourse.course.repos.CourseRepo;
import com.opencourse.course.repos.CourseSubscriptionRepo;
import com.opencourse.course.repos.CourseSubscriptionTypeRepo;
import com.opencourse.course.repos.SectionRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubscriptionService {
    
    private PathService pathService;
    private SectionRepo sectionRepo;
    private CourseSubscriptionRepo subRepo;
    private CourseRepo courseRepo;
    private CourseSubscriptionTypeRepo subTypeRepo;
    
    //only other services
    public boolean userHasAccessToSection(Long sectionId,Long userId){
        
        Section s=sectionRepo.findById(sectionId)
        .orElseThrow(()->new SectionNotFoundException(sectionId));

        Course course=s.getCourse();

        //course is not active and user not creator
        if(!course.isActive() && !userId.equals(course.getCreatorId()))
        return false;

        //user created course 
        if(userId.equals(course.getCreatorId()))
        return true;

        //course is free
        if(course.isFree())
        return true;

        //user is subsribed to course
        Optional<CourseSubscription> opcs = subRepo.findByCourseIdAndUserId(course.getId(), userId);
        if(opcs.isPresent()){
            CourseSubscription cs=opcs.get();
            int durationMonths=cs.getType().getDurationByMonth();
            if(
                LocalDateTime.now()
                .isBefore(cs.getDate().plusMonths(durationMonths))

            )
            return true;
        }

        //can access path
        return pathService.userHasAccessToCourse(course.getId(),userId);

    }


    //only authentic users
    public void subscrib(ChargeRequestDto chargeRequestDto,Long userId,Long courseId,Long subTypeId){
        //make sure course exists
        Course course=courseRepo.findById(courseId)
        .orElseThrow(()->new CourseNotFoundException(courseId));

        //make sure course is paid
        if(course.isFree())
        throw new FreeCourseSubscriptionException(courseId);

        //make sure subscription type exists
        CourseSubscriptionType type=subTypeRepo.findById(subTypeId)
        .orElseThrow(()->new SubscriptionTypeNotFoundException(subTypeId));

        //make sure the amount is correct
        if( chargeRequestDto.getAmount() != type.getPrice())
        throw new UncorrectAmountException();

        //make sure user is not currently  subscribed to course
        Optional<CourseSubscription> opcSub=subRepo.findByCourseIdAndUserId(courseId, userId);
        if(opcSub.isPresent()){
            LocalDateTime subscirptionDate=opcSub.get().getDate();
            short duration=opcSub.get().getType().getDurationByMonth();
            if(
                LocalDateTime.now().isBefore(subscirptionDate.plusMonths(duration))
            )
            throw new UserAlreadySubscribedException(userId,courseId); 
        }

        //test api key
        Stripe.apiKey="sk_test_51Kws3yH4Kp710PsCxU8AakXu1lRPtv9IUAv4N3Jx1ksy4a4snFfjKXCmJhvoq1QCBXU62wHXRm1t7UfzXEDZ9n1v005ZQNxCms";
        

        //take care of the currency problem//
        Map<String,Object> chargeParam=new HashMap<>();
        chargeParam.put("amount", type.getPrice());
        chargeParam.put("currency", chargeRequestDto.getCurrency());
        chargeParam.put("description", "subscription for course  : " + courseId + " , user : " + userId);
        chargeParam.put("source", chargeRequestDto.getStripeToken());

        try {
            Charge charge = Charge.create(chargeParam);

            //if amount is paid
            if(charge.getPaid()){
                CourseSubscription subscription=new CourseSubscription();
                subscription.setCourse(course);
                subscription.setDate(LocalDateTime.now());
                subscription.setType(type);
                subscription.setUserId(userId);
                subRepo.save(subscription);
            }else{
                throw new PaymentException();
            }
        } catch (StripeException e) {
            throw new PaymentException();
        }
    }
    
}
