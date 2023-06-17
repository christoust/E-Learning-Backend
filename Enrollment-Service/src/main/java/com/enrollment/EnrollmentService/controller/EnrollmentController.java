package com.enrollment.EnrollmentService.controller;

import com.enrollment.EnrollmentService.dto.EnrollmentRequest;
import com.enrollment.EnrollmentService.exception.CourseNotFoundException;
import com.enrollment.EnrollmentService.exception.EnrollmentExistsException;
import com.enrollment.EnrollmentService.exception.UserNotFoundException;
import com.enrollment.EnrollmentService.model.Enrollment;
import com.enrollment.EnrollmentService.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public ResponseEntity<Enrollment> enrollUserInCourse(@RequestBody EnrollmentRequest enrollmentRequest) {
        try {
            // Call the enrollment service to create the enrollment
            Enrollment enrolled = enrollmentService.createEnrollment(enrollmentRequest.getUserId(), enrollmentRequest.getCourseId());

            // Return the created enrollment in the response
            return ResponseEntity.status(HttpStatus.CREATED).body(enrolled);
        } catch (UserNotFoundException | CourseNotFoundException | EnrollmentExistsException e) {
            // Handle any exceptions and return an appropriate error response


            // Return a generic error response if the exception type is not handled
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
