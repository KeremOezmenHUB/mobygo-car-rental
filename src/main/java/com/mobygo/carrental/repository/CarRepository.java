package com.mobygo.carrental.repository;

import com.mobygo.carrental.model.Car;
import com.mobygo.carrental.model.CarCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByLocationId(Long locationId);
    List<Car> findByCategory(CarCategory category);
    List<Car> findByLocationIdAndCategory(Long locationId, CarCategory category);
}
