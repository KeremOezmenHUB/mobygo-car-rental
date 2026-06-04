package com.mobygo.carrental.service;

import com.mobygo.carrental.exception.ResourceNotFoundException;
import com.mobygo.carrental.model.Car;
import com.mobygo.carrental.model.CarCategory;
import com.mobygo.carrental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Car", id));
    }

    public List<Car> getCarsByLocation(Long locationId) {
        return carRepository.findByLocationId(locationId);
    }

    public List<Car> getCarsByCategory(CarCategory category) {
        return carRepository.findByCategory(category);
    }

    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    public Car updateCar(Long id, Car updated) {
        Car car = getCarById(id);
        car.setBrand(updated.getBrand());
        car.setModel(updated.getModel());
        car.setLicensePlate(updated.getLicensePlate());
        car.setCategory(updated.getCategory());
        if (updated.getLocation() != null) {
            car.setLocation(updated.getLocation());
        }
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new ResourceNotFoundException("Car", id);
        }
        carRepository.deleteById(id);
    }
}
