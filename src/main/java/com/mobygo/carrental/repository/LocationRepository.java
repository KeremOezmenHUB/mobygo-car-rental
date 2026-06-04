package com.mobygo.carrental.repository;

import com.mobygo.carrental.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByCity(String city);
}
