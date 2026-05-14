package com.mobygo.car_rental.repository;

import com.mobygo.car_rental.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /**
     * Find all cars with a specific status (Available, Rented, Maintenance)
     */
    List<Car> findByStatus(String status);

    /**
     * Find all cars at a specific station
     */
    List<Car> findByStationId(Long stationId);

    /**
     * Find car by license plate (unique identifier)
     */
    Car findByLicensePlate(String licensePlate);
}
