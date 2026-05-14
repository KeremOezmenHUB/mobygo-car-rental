package com.mobygo.car_rental.repository;

import com.mobygo.car_rental.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Find all bookings for a specific user
     */
    List<Booking> findByUserId(Long userId);

    /**
     * Find all bookings for a specific car
     */
    List<Booking> findByCarId(Long carId);

    /**
     * Check for overlapping bookings for a car within a date range
     * Returns bookings that overlap with the requested period
     */
    @Query("SELECT b FROM Booking b WHERE b.car.id = :carId " +
           "AND ((b.startDate <= :endDate) AND (b.endDate >= :startDate))")
    List<Booking> findOverlappingBookings(
            @Param("carId") Long carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
