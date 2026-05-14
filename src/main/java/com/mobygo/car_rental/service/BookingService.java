package com.mobygo.car_rental.service;

import com.mobygo.car_rental.model.Booking;
import com.mobygo.car_rental.model.Car;
import com.mobygo.car_rental.model.User;
import com.mobygo.car_rental.repository.BookingRepository;
import com.mobygo.car_rental.repository.CarRepository;
import com.mobygo.car_rental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Get booking by ID
     */
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    /**
     * Get all bookings for a specific user
     */
    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    /**
     * Get all bookings for a specific car
     */
    public List<Booking> getBookingsByCar(Long carId) {
        return bookingRepository.findByCarId(carId);
    }

    /**
     * CRITICAL BUSINESS RULE:
     * Create a new booking with strict validation:
     * A) Check if the Car is currently 'Available'
     * B) Check if the requested startDate and endDate don't overlap with existing bookings for that car
     * C) If validation fails, throw IllegalStateException
     *
     * @param booking The booking to create
     * @return The created booking
     * @throws IllegalStateException If car is not available or dates overlap
     */
    public Booking createBooking(Booking booking) {
        // Validation: Check required fields
        if (booking.getUser() == null || booking.getUser().getId() == null) {
            throw new IllegalArgumentException("User is required for booking");
        }

        if (booking.getCar() == null || booking.getCar().getId() == null) {
            throw new IllegalArgumentException("Car is required for booking");
        }

        if (booking.getStartDate() == null) {
            throw new IllegalArgumentException("Start date is required");
        }

        if (booking.getEndDate() == null) {
            throw new IllegalArgumentException("End date is required");
        }

        // Validate date logic
        if (booking.getStartDate().isAfter(booking.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        if (booking.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        // CRITICAL RULE A: Check if Car is Available
        Car car = carRepository.findById(booking.getCar().getId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found with ID: " + booking.getCar().getId()));

        if (!"Available".equalsIgnoreCase(car.getStatus())) {
            throw new IllegalStateException(
                    "Cannot book car with ID " + car.getId() + ". " +
                    "Current status is '" + car.getStatus() + "', but must be 'Available'."
            );
        }

        // CRITICAL RULE B: Check for overlapping bookings
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(
                booking.getCar().getId(),
                booking.getStartDate(),
                booking.getEndDate()
        );

        if (!overlappingBookings.isEmpty()) {
            StringBuilder conflictDetails = new StringBuilder();
            for (Booking existingBooking : overlappingBookings) {
                conflictDetails.append(String.format(
                        "Booking ID %d (from %s to %s) ",
                        existingBooking.getId(),
                        existingBooking.getStartDate(),
                        existingBooking.getEndDate()
                ));
            }

            throw new IllegalStateException(
                    "Cannot book car with ID " + car.getId() + " for the requested period. " +
                    "The following existing bookings overlap with your requested dates " +
                    "(" + booking.getStartDate() + " to " + booking.getEndDate() + "): " +
                    conflictDetails.toString()
            );
        }

        // Validate user exists
        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + booking.getUser().getId()));

        // Calculate total price if not already set
        if (booking.getTotalPrice() == null) {
            long days = booking.getEndDate().toEpochDay() - booking.getStartDate().toEpochDay();
            // Example: €50 per day (can be adjusted based on car category)
            booking.setTotalPrice(days * 50.0);
        }

        // Save the booking
        return bookingRepository.save(booking);
    }

    /**
     * Update a booking (with same validation rules)
     */
    public Booking updateBooking(Long id, Booking bookingDetails) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);

        if (bookingOptional.isEmpty()) {
            throw new RuntimeException("Booking not found with ID: " + id);
        }

        Booking booking = bookingOptional.get();

        if (bookingDetails.getStartDate() != null) {
            booking.setStartDate(bookingDetails.getStartDate());
        }
        if (bookingDetails.getEndDate() != null) {
            booking.setEndDate(bookingDetails.getEndDate());
        }
        if (bookingDetails.getTotalPrice() != null) {
            booking.setTotalPrice(bookingDetails.getTotalPrice());
        }

        return bookingRepository.save(booking);
    }

    /**
     * Delete a booking
     */
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    /**
     * Check if a car is available for a specific date range
     * (Utility method for frontend)
     */
    public boolean isCarAvailable(Long carId, LocalDate startDate, LocalDate endDate) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found with ID: " + carId));

        if (!"Available".equalsIgnoreCase(car.getStatus())) {
            return false;
        }

        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(carId, startDate, endDate);
        return overlappingBookings.isEmpty();
    }
}
