package com.mobygo.car_rental.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;

    // Many bookings can belong to one user (Hier ist die neue AppUser-Verknüpfung)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    // Many bookings can be made for one car (over time)
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Booking() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Angepasster Getter für AppUser
    public AppUser getUser() {
        return user;
    }

    // Angepasster Setter für AppUser
    public void setUser(AppUser user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}