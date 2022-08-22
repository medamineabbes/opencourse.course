package com.opencourse.course.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opencourse.course.entities.CourseSubscription;

@Repository
public interface CourseSubscriptionRepo extends JpaRepository<CourseSubscription,Long>{
    Optional<CourseSubscription> findByCourseIdAndUserId(Long courseId,Long userId);
}
