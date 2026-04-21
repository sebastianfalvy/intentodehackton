package org.e2e.labe2e01.vehicle.infrastructure;

import org.e2e.labe2e01.utils.parameters.vehicle.VehicleParameterResolver;
import org.e2e.labe2e01.utils.parameters.vehicle.annotations.BasicVehicle;
import org.e2e.labe2e01.utils.parameters.vehicle.annotations.PersistedVehicle;
import org.e2e.labe2e01.utils.parameters.vehicle.annotations.PersistedVehicleSet;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith({VehicleParameterResolver.class})
class VehicleRepositoryTest {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSaveVehicle(@BasicVehicle Vehicle vehicle) {
        // Act
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // Assert
        assertNotNull(savedVehicle);
        assertNotNull(savedVehicle.getId());
        assertEquals("Toyota", savedVehicle.getBrand());
        assertEquals("Corolla", savedVehicle.getModel());
        assertEquals("ABC-1234", savedVehicle.getLicensePlate());
        assertEquals(2000, savedVehicle.getFabricationYear());
        assertEquals(5, savedVehicle.getCapacity());
    }

    @Test
    public void testFindById(@PersistedVehicle Vehicle vehicle) {
        // Act
        Vehicle foundVehicle = vehicleRepository.findById(vehicle.getId()).orElse(null);

        // Assert
        assertNotNull(foundVehicle);
        assertEquals(vehicle.getId(), foundVehicle.getId());
        assertEquals(vehicle.getBrand(), foundVehicle.getBrand());
        assertEquals(vehicle.getLicensePlate(), foundVehicle.getLicensePlate());
    }

    @Test
    public void testFindByLicensePlate(@PersistedVehicle Vehicle vehicle) {
        // Act
        Vehicle foundVehicle = vehicleRepository.findByLicensePlate(vehicle.getLicensePlate()).orElse(null);

        // Assert
        assertNotNull(foundVehicle);
        assertEquals(vehicle.getId(), foundVehicle.getId());
        assertEquals(vehicle.getBrand(), foundVehicle.getBrand());
        assertEquals(vehicle.getLicensePlate(), foundVehicle.getLicensePlate());
    }

    @Test
    public void testFindAllByBrand(
            @PersistedVehicleSet(count = 5, brand = "Tesla") List<Vehicle> vehicles
    ) {
        // Arrange: Add one vehicle from a different brand
        Vehicle diffVehicle = new Vehicle();
        diffVehicle.setBrand("Toyota");
        diffVehicle.setModel("Yaris");
        diffVehicle.setLicensePlate("LPD-098");
        diffVehicle.setCapacity(10);
        diffVehicle.setFabricationYear(2010);

        vehicleRepository.save(diffVehicle);

        // Act
        List<Vehicle> foundVehicles = vehicleRepository.findAllByBrand("Tesla");

        // Assert
        assertFalse(foundVehicles.isEmpty());
        assertEquals(5, foundVehicles.size());
        assertEquals(vehicles, foundVehicles);
    }

    @Test
    public void testLicensePlateUniqueConstraint() {
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setBrand("Tesla");
        vehicle1.setModel("Model S");
        vehicle1.setLicensePlate("UNI123");
        vehicle1.setFabricationYear(2022);
        vehicle1.setCapacity(5);
        entityManager.persistAndFlush(vehicle1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setBrand("Tesla");
        vehicle2.setModel("Model 3");
        vehicle2.setLicensePlate("UNI123");
        vehicle2.setFabricationYear(2023);
        vehicle2.setCapacity(5);

        assertThrows(ConstraintViolationException.class, () -> entityManager.persistAndFlush(vehicle2));
    }
}
