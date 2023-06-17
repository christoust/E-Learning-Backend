package com.rating.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.rating.dto.Course;
import com.rating.dto.RatingFeedback;
import com.rating.dto.RatingRequest;
import com.rating.dto.User;
import com.rating.exception.EnrollmentExistsException;
import com.rating.exception.RatingNotFoundException;
import org.aspectj.lang.reflect.NoSuchAdviceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rating.model.Rating;
import com.rating.repository.RatingRepository;
import org.springframework.web.client.RestTemplate;

@Service
public class RatingService {
    @Autowired
    private  RatingRepository ratingRepository;

    @Autowired
    private RestTemplate restTemplate;


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
    public Rating saveRating(Rating rating) {
        // Fetch user details from user microservice
        User user = fetchUserDetails(rating.getUserId());

        // Fetch course details from course microservice
        Course course = fetchCourseDetails(rating.getCourseId());
        if (ratingRepository.existsByCourseIdAndUserId(course.getId(), user.getId())){
            throw new EnrollmentExistsException("Enrollment already exists for courseId: " + course.getId() +
                    " and userId: " + user.getId());
        }


        rating.setDate(LocalDate.now());


        // Save the enrollment in the enrollment microservice
        Rating savedRating = ratingRepository.save(rating);

        return savedRating;
    }

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating getRatingById(Long id) throws NoSuchAdviceException {
        return ratingRepository.findById(id).orElseThrow(()-> new NoSuchAdviceException("Rating not found"));
    }
    
    public Rating updateRating(Long id, Rating updatedRating) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Rating not found"));

//        rating.setCourseId(updatedRating.getCourseId());
        rating.setFeedback(updatedRating.getFeedback());
        rating.setRating(updatedRating.getRating());
//        rating.setUserId(updatedRating.getUserId());

        return ratingRepository.save(rating);
    }
    public List<RatingFeedback> getRatingsAndFeedbackByCourseId(Long courseId) {
        List<Rating> ratings = ratingRepository.findByCourseId(courseId);
        List<RatingFeedback> ratingFeedbackList = new ArrayList<>();

        for (Rating rating : ratings) {
            RatingFeedback ratingFeedback = new RatingFeedback();
            ratingFeedback.setRating(rating.getRating());
            ratingFeedback.setFeedback(rating.getFeedback());
            ratingFeedbackList.add(ratingFeedback);
        }

        return ratingFeedbackList;
    }

    public RatingFeedback getRatingAndFeedbackForCourseByUser(Long userId, Long courseId) {
        Rating existingRating = ratingRepository.findByCourseIdAndUserId(courseId, userId);
        if (existingRating == null) {
            throw new RatingNotFoundException("Rating not found for userId: " + userId + " and courseId: " + courseId);
        }

        RatingFeedback ratingFeedbackResponse = new RatingFeedback();
        ratingFeedbackResponse.setRating(existingRating.getRating());
        ratingFeedbackResponse.setFeedback(existingRating.getFeedback());

        return ratingFeedbackResponse;
    }

}
    




