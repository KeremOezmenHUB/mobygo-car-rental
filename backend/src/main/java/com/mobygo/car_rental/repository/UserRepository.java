package com.mobygo.car_rental.repository;

import com.mobygo.car_rental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by email (unique identifier)
     */
    User findByEmail(String email);

    /**
     * Check if a user exists by email
     */
    boolean existsByEmail(String email);
}
