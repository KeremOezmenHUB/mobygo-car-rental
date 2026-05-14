package com.mobygo.car_rental.service;

import com.mobygo.car_rental.model.User;
import com.mobygo.car_rental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Get user by email
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Create a new user
     */
    public User createUser(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("USER");
        }

        return userRepository.save(user);
    }

    /**
     * Update user details
     */
    public User updateUser(Long id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        User user = userOptional.get();

        if (userDetails.getFirstName() != null) {
            user.setFirstName(userDetails.getFirstName());
        }
        if (userDetails.getLastName() != null) {
            user.setLastName(userDetails.getLastName());
        }
        if (userDetails.getEmail() != null) {
            // Check if new email is already taken by another user
            User existingUser = userRepository.findByEmail(userDetails.getEmail());
            if (existingUser != null && !existingUser.getId().equals(id)) {
                throw new IllegalArgumentException("Email already exists: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }

        return userRepository.save(user);
    }

    /**
     * Delete a user
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
