package com.mobygo.car_rental.controller;

import com.mobygo.car_rental.model.Booking;
import com.mobygo.car_rental.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(
        origins = {"http://localhost:3000", "http://localhost:5500", "http://127.0.0.1:5500"},
        allowedHeaders = {"Authorization", "Content-Type"},
        exposedHeaders = {"Authorization", "WWW-Authenticate"},
        allowCredentials = "true",
        maxAge = 3600
)
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * GET /api/bookings
     * Returns all bookings
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    /**
     * GET /api/bookings/{id}
     * Returns a specific booking by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/bookings/user/{userId}
     * Returns booking history for a specific user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }

    /**
     * GET /api/bookings/car/{carId}
     * Returns all bookings for a specific car
     */
    @GetMapping("/car/{carId}")
    public ResponseEntity<List<Booking>> getBookingsByCar(@PathVariable Long carId) {
        List<Booking> bookings = bookingService.getBookingsByCar(carId);
        return ResponseEntity.ok(bookings);
    }

    /**
     * GET /api/bookings/availability/{carId}
     * Check if a car is available for a specific date range
     * Query params: startDate and endDate (format: YYYY-MM-DD)
     */
    @GetMapping("/availability/{carId}")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @PathVariable Long carId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            boolean available = bookingService.isCarAvailable(carId, start, end);

            Map<String, Object> response = new HashMap<>();
            response.put("carId", carId);
            response.put("startDate", startDate);
            response.put("endDate", endDate);
            response.put("available", available);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid date format or car ID");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * POST /api/bookings
     * Creates a new reservation with strict business rule validation:
     * - Car must be 'Available'
     * - No overlapping bookings for the requested date range
     *
     * Returns: 201 CREATED on success
     * Returns: 400 BAD REQUEST if validation fails
     * Returns: 409 CONFLICT if business rule is violated (car not available or dates overlap)
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(@RequestBody Booking booking) {
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "message", "Booking created successfully",
                            "bookingId", createdBooking.getId(),
                            "totalPrice", createdBooking.getTotalPrice()
                    )
            );
        } catch (IllegalStateException e) {
            // Business rule violation: Car not available or dates overlap
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", e.getMessage())
            );
        } catch (IllegalArgumentException e) {
            // Validation error: Missing or invalid fields
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "An error occurred while creating the booking: " + e.getMessage())
            );
        }
    }

    /**
     * PUT /api/bookings/{id}
     * Updates a booking
     */
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking bookingDetails) {
        try {
            Booking updatedBooking = bookingService.updateBooking(id, bookingDetails);
            return ResponseEntity.ok(updatedBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/bookings/{id}
     * Cancels a booking
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Global exception handler for booking-related errors
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("error", e.getMessage())
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
}
