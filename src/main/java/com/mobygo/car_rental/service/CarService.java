package com.mobygo.car_rental.service;

import com.mobygo.car_rental.model.Car;
import com.mobygo.car_rental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    /**
     * Get all cars
     */
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    /**
     * Get car by ID
     */
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    /**
     * Get all cars by status (Available, Rented, Maintenance)
     */
    public List<Car> getCarsByStatus(String status) {
        return carRepository.findByStatus(status);
    }

    /**
     * Get all available cars
     */
    public List<Car> getAvailableCars() {
        return carRepository.findByStatus("Available");
    }

    /**
     * Get all cars at a specific station
     */
    public List<Car> getCarsByStation(Long stationId) {
        return carRepository.findByStationId(stationId);
    }

    /**
     * Create a new car
     */
    public Car createCar(Car car) {
        if (car.getLicensePlate() == null || car.getLicensePlate().trim().isEmpty()) {
            throw new IllegalArgumentException("License plate is required");
        }
        if (car.getStatus() == null || car.getStatus().trim().isEmpty()) {
            car.setStatus("Available");
        }
        return carRepository.save(car);
    }

    /**
     * Update car details
     */
    public Car updateCar(Long id, Car carDetails) {
        Optional<Car> carOptional = carRepository.findById(id);

        if (carOptional.isEmpty()) {
            throw new RuntimeException("Car not found with ID: " + id);
        }

        Car car = carOptional.get();
        
        if (carDetails.getLicensePlate() != null) {
            car.setLicensePlate(carDetails.getLicensePlate());
        }
        if (carDetails.getModel() != null) {
            car.setModel(carDetails.getModel());
        }
        if (carDetails.getCategory() != null) {
            car.setCategory(carDetails.getCategory());
        }
        if (carDetails.getStatus() != null) {
            car.setStatus(carDetails.getStatus());
        }
        if (carDetails.getStation() != null) {
            car.setStation(carDetails.getStation());
        }

        return carRepository.save(car);
    }

    /**
     * Delete a car
     */
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}
