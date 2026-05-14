package com.mobygo.car_rental.service;

import com.mobygo.car_rental.model.Station;
import com.mobygo.car_rental.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    /**
     * Get all stations
     */
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    /**
     * Get station by ID
     */
    public Optional<Station> getStationById(Long id) {
        return stationRepository.findById(id);
    }

    /**
     * Get station by name
     */
    public Station getStationByName(String name) {
        return stationRepository.findByName(name);
    }

    /**
     * Get all stations in a specific city
     */
    public List<Station> getStationsByCity(String city) {
        return stationRepository.findByCity(city);
    }

    /**
     * Create a new station
     */
    public Station createStation(Station station) {
        if (station.getName() == null || station.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Station name is required");
        }
        if (station.getCity() == null || station.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        return stationRepository.save(station);
    }

    /**
     * Update station details
     */
    public Station updateStation(Long id, Station stationDetails) {
        Optional<Station> stationOptional = stationRepository.findById(id);

        if (stationOptional.isEmpty()) {
            throw new RuntimeException("Station not found with ID: " + id);
        }

        Station station = stationOptional.get();

        if (stationDetails.getName() != null) {
            station.setName(stationDetails.getName());
        }
        if (stationDetails.getCity() != null) {
            station.setCity(stationDetails.getCity());
        }
        if (stationDetails.getAddress() != null) {
            station.setAddress(stationDetails.getAddress());
        }

        return stationRepository.save(station);
    }

    /**
     * Delete a station
     */
    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }
}
