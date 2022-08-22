package com.opencourse.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opencourse.course.entities.CourseTopic;

@Repository
public interface CourseTopicRepo extends JpaRepository<CourseTopic,Long>{
    
}
