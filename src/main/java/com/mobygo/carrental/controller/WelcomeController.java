package com.mobygo.carrental.controller;

import com.mobygo.carrental.model.CarCategory;
import com.mobygo.carrental.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Welcome", description = "Public information endpoints")
public class WelcomeController {

    @Autowired
    private RentalService rentalService;

    @GetMapping("/")
    @Operation(summary = "Welcome message")
    public String welcome() {
        return "Welcome to MobyGo Car Rental!";
    }

    @GetMapping("/rates")
    @Operation(summary = "Get daily rental rates per car category")
    public Map<CarCategory, Double> getDailyRates() {
        return rentalService.getDailyRates();
    }
}
