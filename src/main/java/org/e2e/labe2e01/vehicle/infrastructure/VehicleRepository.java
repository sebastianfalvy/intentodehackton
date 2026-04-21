package org.e2e.labe2e01.vehicle.infrastructure;

import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    List<Vehicle> findAllByBrand(String brand);
}
