package com.user.UserService.service;

// UserService.java

import com.user.UserService.dto.UserDTO;
import com.user.UserService.exception.UserNotFoundException;
import com.user.UserService.model.User;
import com.user.UserService.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserDTO userDTO) {


        // Convert UserDTO to User entity
        User u = new User(0L,userDTO.getUsername(),userDTO.getPassword());

        return userRepository.save(u);
    }

    public User getUserById(Long userId) {
        // Retrieve the user from the database by ID
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

//    public Object getUserByUsername(String username) {
//        // Retrieve the user from the database by username
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//    }

    public List<User> getAllUsers() {
        // Retrieve all users from the database
        return userRepository.findAll();
    }

    public User updateUser(Long userId, UserDTO userDTO) {
        // Retrieve the user from the database by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Update user details based on the UserDTO
        user.setUsername(userDTO.getUsername());

        // Update other user attributes

        // Save the updated user in the database
        return userRepository.save(user);
    }

    // Other methods for user-related operations

    // ...
}

