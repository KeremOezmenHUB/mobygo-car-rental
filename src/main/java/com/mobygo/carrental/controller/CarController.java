package com.mobygo.carrental.controller;

import com.mobygo.carrental.model.Car;
import com.mobygo.carrental.model.CarCategory;
import com.mobygo.carrental.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@Tag(name = "Cars", description = "Car management endpoints")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    @Operation(summary = "List all cars")
    public ResponseEntity<List<Car>> getAllCars(
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) CarCategory category) {
        if (locationId != null) return ResponseEntity.ok(carService.getCarsByLocation(locationId));
        if (category != null) return ResponseEntity.ok(carService.getCarsByCategory(category));
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by ID")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new car (admin)")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        return ResponseEntity.ok(carService.createCar(car));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a car (admin)")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car car) {
        return ResponseEntity.ok(carService.updateCar(id, car));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car (admin)")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
