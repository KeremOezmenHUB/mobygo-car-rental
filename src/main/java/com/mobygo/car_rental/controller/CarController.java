package com.mobygo.car_rental.controller;

import com.mobygo.car_rental.model.Car;
import com.mobygo.car_rental.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CarController {

    @Autowired
    private CarService carService;

    /**
     * GET /api/cars
     * Returns list of all cars with their station data
     */
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    /**
     * GET /api/cars/{id}
     * Returns a specific car by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> car = carService.getCarById(id);
        return car.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/cars/status/{status}
     * Returns all cars with a specific status (Available, Rented, Maintenance)
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Car>> getCarsByStatus(@PathVariable String status) {
        List<Car> cars = carService.getCarsByStatus(status);
        return ResponseEntity.ok(cars);
    }

    /**
     * GET /api/cars/station/{stationId}
     * Returns all cars at a specific station
     */
    @GetMapping("/station/{stationId}")
    public ResponseEntity<List<Car>> getCarsByStation(@PathVariable Long stationId) {
        List<Car> cars = carService.getCarsByStation(stationId);
        return ResponseEntity.ok(cars);
    }

    /**
     * POST /api/cars
     * Creates a new car
     */
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        try {
            Car createdCar = carService.createCar(car);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/cars/{id}
     * Updates a car's details/status
     */
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        try {
            Car updatedCar = carService.updateCar(id, carDetails);
            return ResponseEntity.ok(updatedCar);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/cars/{id}
     * Deletes a car
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
