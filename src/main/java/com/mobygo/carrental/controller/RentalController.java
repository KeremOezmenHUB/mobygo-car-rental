package com.mobygo.carrental.controller;

import com.mobygo.carrental.model.Rental;
import com.mobygo.carrental.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rentals", description = "Car rental booking endpoints")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping
    @Operation(summary = "Create a rental — guest (name+email) or registered user (userId)")
    public ResponseEntity<Rental> createRental(@RequestBody RentalRequest request) {
        Rental rental = rentalService.createRental(
            request.getCarId(),
            request.getPickupLocationId(),
            request.getDropoffLocationId(),
            request.getCustomerName(),
            request.getCustomerEmail(),
            request.getUserId(),
            request.getStartDate(),
            request.getEndDate()
        );
        return ResponseEntity.ok(rental);
    }

    @GetMapping
    @Operation(summary = "List all rentals (admin)")
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "List rentals for a specific user")
    public ResponseEntity<List<Rental>> getUserRentals(@PathVariable String username) {
        return ResponseEntity.ok(rentalService.getRentalsByUser(username));
    }
}
