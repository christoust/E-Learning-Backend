package com.enrollment.EnrollmentService.service;

import com.enrollment.EnrollmentService.dto.User;
import com.enrollment.EnrollmentService.dto.Course;
import com.enrollment.EnrollmentService.exception.EnrollmentExistsException;
import com.enrollment.EnrollmentService.model.Enrollment;
import com.enrollment.EnrollmentService.repository.EnrolmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {
    @Autowired
    private  RestTemplate restTemplate;
   @Autowired
   private EnrolmentRepository  enrolmentRepository;

        public User fetchUserDetails(Long userId) {
            String userMicroserviceUrl = "http://localhost:9001/users/{userId}";
            ResponseEntity<User> response = restTemplate.exchange(userMicroserviceUrl, HttpMethod.GET, null, User.class, userId);
            return response.getBody();
        }

        public Course fetchCourseDetails(Long courseId) {
            String courseMicroserviceUrl = "http://localhost:9002/courses/{courseId}";
            ResponseEntity<Course> response = restTemplate.exchange(courseMicroserviceUrl, HttpMethod.GET, null, Course.class, courseId);
            return response.getBody();
        }
    public Enrollment createEnrollment(Long userId,Long courseId)throws EnrollmentExistsException {
        // Fetch user details from user microservice
        User user = fetchUserDetails(userId);

        // Fetch course details from course microservice
        Course course = fetchCourseDetails(courseId);
        if (enrolmentRepository.existsByCourseIdAndUserId(course.getId(), user.getId())){
            throw new  EnrollmentExistsException("Enrollment already exists for courseId: " + course.getId() +
                    " and userId: " + user.getId());
        }

        // Create an enrollment object
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(user.getId());
        enrollment.setCourseId(course.getId());
        enrollment.setEnrollmentDate(LocalDate.now());

        // Save the enrollment in the enrollment microservice
        Enrollment savedEnrollment = enrolmentRepository.save(enrollment);

        return savedEnrollment;
    }

}
