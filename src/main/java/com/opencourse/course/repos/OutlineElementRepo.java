package com.opencourse.course.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opencourse.course.entities.OutlineElement;

@Repository
public interface OutlineElementRepo extends JpaRepository<OutlineElement,Long>{
    
}
