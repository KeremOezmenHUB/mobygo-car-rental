package com.mobygo.carrental.service;

import com.mobygo.carrental.exception.ResourceNotFoundException;
import com.mobygo.carrental.model.Location;
import com.mobygo.carrental.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Location", id));
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, Location updated) {
        Location location = getLocationById(id);
        location.setName(updated.getName());
        location.setCity(updated.getCity());
        location.setAddress(updated.getAddress());
        return locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Location", id);
        }
        locationRepository.deleteById(id);
    }
}
