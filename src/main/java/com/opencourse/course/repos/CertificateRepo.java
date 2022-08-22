package com.opencourse.course.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.opencourse.course.entities.Certificate;

@Repository
public interface CertificateRepo extends JpaRepository<Certificate,Long> {
    Optional<Certificate> findByCourseIdAndUserId(Long courseId,Long userId);
}
