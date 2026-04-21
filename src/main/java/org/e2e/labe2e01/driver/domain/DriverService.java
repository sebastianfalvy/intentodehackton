package org.e2e.labe2e01.driver.domain;

import lombok.RequiredArgsConstructor;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.coordinate.infrastructure.CoordinateRepository;
import org.e2e.labe2e01.driver.infrastructure.DriverRepository;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;
    private final CoordinateRepository coordinateRepository;

    public Optional<Driver> findById(Long id) {
        return driverRepository.findById(id);
    }

    public Driver save(Driver driver) {
        if (driver.getCreatedAt() == null) {
            driver.setCreatedAt(ZonedDateTime.now());
        }
        return driverRepository.save(driver);
    }

    public void deleteById(Long id) {
        driverRepository.deleteById(id);
    }

    public Driver updateLocation(Long id, Double latitude, Double longitude) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + id));
        Coordinate coordinate = new Coordinate(latitude, longitude);
        coordinateRepository.save(coordinate);
        driver.setCoordinate(coordinate);
        return driverRepository.save(driver);
    }

    public Driver addVehicle(Long driverId, Vehicle vehicle) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));

        if (vehicle.getCategory() == null) {
            vehicle.setCategory(driver.getCategory());
        }

        vehicle.setDriver(driver);
        driver.setVehicle(vehicle);
        return driverRepository.save(driver);
    }
}
