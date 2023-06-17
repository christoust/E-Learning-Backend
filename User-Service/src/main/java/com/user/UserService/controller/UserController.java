package com.user.UserService.controller;

import com.user.UserService.dto.UserDTO;
import com.user.UserService.model.User;
import com.user.UserService.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// UserController.java
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        // Create a new user


        // Return the created user with a 201 Created status
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        // Retrieve the user by ID
        User user = userService.getUserById(userId);

        // Return the user with a 200 OK status
        return ResponseEntity.ok(user);
    }

//    @GetMapping("/username/{username}")
//    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
//        // Retrieve the user by username
//        User user = (User) userService.getUserByUsername(username);
//
//        // Return the user with a 200 OK status
//        return ResponseEntity.ok(user);
//    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // Retrieve all users
        List<User> users = userService.getAllUsers();

        // Return the list of users with a 200 OK status
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        // Update the user with the given ID
        User updatedUser = userService.updateUser(userId, userDTO);

        // Return the updated user with a 200 OK status
        return ResponseEntity.ok(updatedUser);
    }

    // Other API endpoints for user-related operations

    // ...
}

