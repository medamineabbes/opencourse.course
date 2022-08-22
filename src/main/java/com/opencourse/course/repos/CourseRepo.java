package com.opencourse.course.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opencourse.course.entities.Course;

@Repository
public interface CourseRepo extends JpaRepository<Course,Long>{
    public Page<Course> findByActiveAndTitleContaining(boolean active,String title,org.springframework.data.domain.Pageable pageable);
}
