package com.mobygo.carrental.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    private CarCategory category;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonBackReference
    private Location location;

    public Car() {}

    public Car(String brand, String model, String licensePlate, CarCategory category, Location location) {
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.category = category;
        this.location = location;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public CarCategory getCategory() { return category; }
    public void setCategory(CarCategory category) { this.category = category; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}
