package com.mobygo.car_rental.repository;

import com.mobygo.car_rental.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    /**
     * Find a station by name
     */
    Station findByName(String name);

    /**
     * Find all stations in a specific city
     */
    List<Station> findByCity(String city);
}
