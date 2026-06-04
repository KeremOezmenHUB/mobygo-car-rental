package com.mobygo.carrental.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optional: set when booked by a registered user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    // Used for guest bookings (no account required)
    private String customerName;
    private String customerEmail;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "pickup_location_id")
    private Location pickupLocation;

    @ManyToOne
    @JoinColumn(name = "dropoff_location_id")
    private Location dropoffLocation;

    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;

    public Rental() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public Location getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(Location pickupLocation) { this.pickupLocation = pickupLocation; }

    public Location getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(Location dropoffLocation) { this.dropoffLocation = dropoffLocation; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
