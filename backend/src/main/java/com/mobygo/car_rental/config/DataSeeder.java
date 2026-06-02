package com.mobygo.car_rental.config;

import com.mobygo.car_rental.model.AppUser;
import com.mobygo.car_rental.model.Role;
import com.mobygo.car_rental.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initDatabase(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Check if admin exists. If not, create and seed.
            if (userRepository.findByEmail("admin@mobygo.com").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setEmail("admin@mobygo.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("--- Admin user seeded into database ---");
            }

            // 2. Check if a default customer exists. If not, create and seed.
            if (userRepository.findByEmail("customer@mobygo.com").isEmpty()) {
                AppUser customer = new AppUser();
                customer.setEmail("customer@mobygo.com");
                customer.setPassword(passwordEncoder.encode("customer123"));
                customer.setRole(Role.CUSTOMER);
                userRepository.save(customer);
                System.out.println("--- Test customer seeded into database ---");
            }
        };
    }
}
