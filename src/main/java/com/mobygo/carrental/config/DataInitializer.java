package com.mobygo.carrental.config;

import com.mobygo.carrental.model.*;
import com.mobygo.carrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired private LocationRepository locationRepository;
    @Autowired private CarRepository carRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        if (locationRepository.count() > 0) return;

        // Locations
        Location zurich = locationRepository.save(new Location("MobyGo Zurich Airport", "Zurich", "Flughafenstrasse 1, 8058 Zurich"));
        Location basel  = locationRepository.save(new Location("MobyGo Basel Central", "Basel", "Centralbahnplatz 10, 4051 Basel"));
        Location bern   = locationRepository.save(new Location("MobyGo Bern City", "Bern", "Bahnhofplatz 2, 3011 Bern"));

        // Cars
        carRepository.save(new Car("VW",       "Golf",      "ZH 100 AA", CarCategory.ECONOMY, zurich));
        carRepository.save(new Car("Toyota",   "Corolla",   "ZH 200 BB", CarCategory.COMPACT, zurich));
        carRepository.save(new Car("BMW",      "X5",        "BS 300 CC", CarCategory.SUV,     basel));
        carRepository.save(new Car("Mercedes", "E-Class",   "BS 400 DD", CarCategory.LUXURY,  basel));
        carRepository.save(new Car("Ford",     "Transit",   "BE 500 EE", CarCategory.VAN,     bern));
        carRepository.save(new Car("Skoda",    "Octavia",   "BE 600 FF", CarCategory.ECONOMY, bern));
        carRepository.save(new Car("Audi",     "Q7",        "ZH 700 GG", CarCategory.SUV,     zurich));
        carRepository.save(new Car("Porsche",  "Cayenne",   "BS 800 HH", CarCategory.LUXURY,  basel));

        // Users
        userRepository.save(new User("admin", passwordEncoder.encode("admin123"), "ADMIN"));
        userRepository.save(new User("john",  passwordEncoder.encode("user123"),  "USER"));
        userRepository.save(new User("anna",  passwordEncoder.encode("user123"),  "USER"));
    }
}
