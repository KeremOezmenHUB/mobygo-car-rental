package com.mobygo.car_rental.controller;

import com.mobygo.car_rental.model.Station;
import com.mobygo.car_rental.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StationController {

    @Autowired
    private StationService stationService;

    /**
     * GET /api/stations
     * Returns all stations
     */
    @GetMapping
    public ResponseEntity<List<Station>> getAllStations() {
        List<Station> stations = stationService.getAllStations();
        return ResponseEntity.ok(stations);
    }

    /**
     * GET /api/stations/{id}
     * Returns a specific station by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(@PathVariable Long id) {
        Optional<Station> station = stationService.getStationById(id);
        return station.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/stations/name/{name}
     * Returns a station by name
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Station> getStationByName(@PathVariable String name) {
        Station station = stationService.getStationByName(name);
        if (station == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(station);
    }

    /**
     * GET /api/stations/city/{city}
     * Returns all stations in a specific city
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Station>> getStationsByCity(@PathVariable String city) {
        List<Station> stations = stationService.getStationsByCity(city);
        return ResponseEntity.ok(stations);
    }

    /**
     * POST /api/stations
     * Creates a new station
     */
    @PostMapping
    public ResponseEntity<Station> createStation(@RequestBody Station station) {
        try {
            Station createdStation = stationService.createStation(station);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/stations/{id}
     * Updates a station's details
     */
    @PutMapping("/{id}")
    public ResponseEntity<Station> updateStation(@PathVariable Long id, @RequestBody Station stationDetails) {
        try {
            Station updatedStation = stationService.updateStation(id, stationDetails);
            return ResponseEntity.ok(updatedStation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/stations/{id}
     * Deletes a station
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStation(@PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }
}
