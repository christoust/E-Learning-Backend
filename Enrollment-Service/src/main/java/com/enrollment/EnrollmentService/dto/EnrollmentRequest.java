package com.enrollment.EnrollmentService.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequest {


        private Long userId;
        private Long courseId;
}
