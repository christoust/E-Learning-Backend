package com.enrollment.EnrollmentService.repository;

import com.enrollment.EnrollmentService.model.Enrollment;
//import com.enrollment.EnrollmentService.model.Enrolment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrolmentRepository extends JpaRepository<Enrollment,Long> {
    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByUserId(Long userId);

    boolean existsByCourseIdAndUserId(Long courseId, Long userId);
}
