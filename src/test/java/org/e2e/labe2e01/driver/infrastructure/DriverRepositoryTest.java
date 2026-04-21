package org.e2e.labe2e01.driver.infrastructure;

import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.driver.domain.Category;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.utils.parameters.users.UserParameterResolver;
import org.e2e.labe2e01.utils.parameters.users.annotations.BasicDriver;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith({UserParameterResolver.class})
class DriverRepositoryTest {
    @Autowired
    private DriverRepository driverRepository;

    @Test
    @Transactional
    public void shouldSaveValidDriver(@BasicDriver Driver driver){
        // Act
        Driver persistedDriver = driverRepository.save(driver);

        // Assert
        assertNotNull(persistedDriver);
        assertNotNull(persistedDriver.getId());

        assertEquals(Role.DRIVER, persistedDriver.getRole());
        assertEquals(Category.X, persistedDriver.getCategory());
        assertEquals("Jhon", persistedDriver.getFirstName());
        assertEquals("Doe", persistedDriver.getLastName());
        assertEquals("jhon.doe@example.com", persistedDriver.getEmail());
        assertEquals("password123", persistedDriver.getPassword());
        assertEquals("1234567890", persistedDriver.getPhoneNumber());
    }

    @Test
    @Transactional
    public void shouldSaveTransientEntityVehicle(@BasicDriver Driver driver){
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setLicensePlate("XYZ789");
        vehicle.setFabricationYear(2021);
        vehicle.setCapacity(4);

        driver.setVehicle(vehicle);

        // Act
        Driver persistedDriver = driverRepository.save(driver);

        // Assert
        assertNotNull(persistedDriver);
        assertNotNull(persistedDriver.getId());

        assertNotNull(persistedDriver.getVehicle());
        assertNotNull(persistedDriver.getVehicle().getId());

        assertEquals("Toyota", persistedDriver.getVehicle().getBrand());
        assertEquals("Corolla", persistedDriver.getVehicle().getModel());
        assertEquals("XYZ789", persistedDriver.getVehicle().getLicensePlate());
        assertEquals(2021, persistedDriver.getVehicle().getFabricationYear());
        assertEquals(4, persistedDriver.getVehicle().getCapacity());
    }

    @Test
    @Transactional
    public void shouldSaveTransientEntityCoordinate(@BasicDriver Driver driver){
        // Arrange
        Coordinate coordinate = new Coordinate(34.0522, -118.2437);
        driver.setCoordinate(coordinate);

        // Act
        Driver persistedDriver = driverRepository.save(driver);

        // Assert
        assertNotNull(persistedDriver);
        assertNotNull(persistedDriver.getId());

        assertNotNull(persistedDriver.getCoordinate());
        assertNotNull(persistedDriver.getCoordinate().getId());

        assertEquals(34.0522, persistedDriver.getCoordinate().getLatitude());
        assertEquals(-118.2437, persistedDriver.getCoordinate().getLongitude());
    }

    static Stream<Arguments> providerDriverSets() {
        return Stream.of(
                Arguments.of(Category.X, 3),
                Arguments.of(Category.XL, 5),
                Arguments.of(Category.BLACK, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("providerDriverSets")
    public void shouldFindDriversByCategory(Category category, int count){
        // Arrange
        for (int i = 0; i < count; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setBrand("Brand" + i);
            vehicle.setModel("Model" + i);
            vehicle.setLicensePlate("PLATE" + i);
            vehicle.setFabricationYear(2020 + i);
            vehicle.setCapacity(4 + i);

            Driver driver = new Driver();
            driver.setFirstName("Driver" + i);
            driver.setLastName("LastName" + i);
            driver.setEmail("driver" + i + "@example.com");
            driver.setPassword("password" + i);
            driver.setPhoneNumber("123456789" + i);
            driver.setCategory(category);
            driver.setVehicle(vehicle);
            driver.setRole(Role.DRIVER);
            driver.setCreatedAt(ZonedDateTime.now());

            driverRepository.save(driver);
        }

        // Act
        List<Driver> drivers = driverRepository.findAllByCategory(category);

        // Assert
        assertNotNull(drivers);
        assertEquals(count, drivers.size());
        for (Driver driver : drivers) {
            assertEquals(category, driver.getCategory());
            assertNotNull(driver.getVehicle());
            assertNotNull(driver.getVehicle().getId());
        }
    }
}
