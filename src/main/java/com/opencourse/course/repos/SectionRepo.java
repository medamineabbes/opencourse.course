package com.opencourse.course.repos;

import com.opencourse.course.entities.Section;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepo extends JpaRepository<Section,Long>{
    
}
