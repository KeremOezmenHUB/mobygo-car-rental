package com.mobygo.carrental.repository;

import com.mobygo.carrental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUserUsername(String username);

    @Query("SELECT r FROM Rental r WHERE r.car.id = :carId " +
           "AND r.startDate < :endDate AND r.endDate > :startDate")
    List<Rental> findOverlappingRentals(@Param("carId") Long carId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);
}
