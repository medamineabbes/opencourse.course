package com.opencourse.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opencourse.course.entities.CourseSubscriptionType;

@Repository
public interface CourseSubscriptionTypeRepo extends JpaRepository<CourseSubscriptionType,Long>{
    
}
