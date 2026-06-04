package com.mobygo.carrental.service;

import com.mobygo.carrental.exception.ResourceNotFoundException;
import com.mobygo.carrental.model.*;
import com.mobygo.carrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class RentalService {

    // Business rule: daily rental rates per category in CHF
    private static final Map<CarCategory, Double> DAILY_RATES = Map.of(
        CarCategory.ECONOMY, 50.0,
        CarCategory.COMPACT, 80.0,
        CarCategory.SUV,     120.0,
        CarCategory.LUXURY,  200.0,
        CarCategory.VAN,     100.0
    );

    @Autowired private RentalRepository rentalRepository;
    @Autowired private CarRepository carRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private UserRepository userRepository;

    public Rental createRental(Long carId, Long pickupLocationId, Long dropoffLocationId,
                               String customerName, String customerEmail, Long userId,
                               LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (!startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }
        if ((customerName == null || customerName.isBlank()) && userId == null) {
            throw new IllegalArgumentException("Customer name is required");
        }

        Car car = carRepository.findById(carId)
            .orElseThrow(() -> new ResourceNotFoundException("Car", carId));

        // Business rule: no overlapping rentals for the same car
        List<Rental> conflicts = rentalRepository.findOverlappingRentals(carId, startDate, endDate);
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException(
                "Car " + car.getBrand() + " " + car.getModel() +
                " is already booked from " + conflicts.get(0).getStartDate() +
                " to " + conflicts.get(0).getEndDate());
        }

        Location pickup = locationRepository.findById(pickupLocationId)
            .orElseThrow(() -> new ResourceNotFoundException("Pickup location", pickupLocationId));
        Location dropoff = locationRepository.findById(dropoffLocationId)
            .orElseThrow(() -> new ResourceNotFoundException("Dropoff location", dropoffLocationId));

        // Business rule: calculate total price = daily rate × number of days
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        double dailyRate = DAILY_RATES.getOrDefault(car.getCategory(), 80.0);
        double totalPrice = days * dailyRate;

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setPickupLocation(pickup);
        rental.setDropoffLocation(dropoff);
        rental.setStartDate(startDate);
        rental.setEndDate(endDate);
        rental.setTotalPrice(totalPrice);

        // Attach user if provided, otherwise store guest info
        if (userId != null) {
            userRepository.findById(userId).ifPresent(rental::setUser);
        } else {
            rental.setCustomerName(customerName);
            rental.setCustomerEmail(customerEmail);
        }

        return rentalRepository.save(rental);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getRentalsByUser(String username) {
        return rentalRepository.findByUserUsername(username);
    }

    public Map<CarCategory, Double> getDailyRates() {
        return DAILY_RATES;
    }
}
