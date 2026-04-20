package org.e2e.labe2e01.driver.domain;

import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.utils.parameters.users.UserParameterResolver;
import org.e2e.labe2e01.utils.parameters.users.annotations.BasicDriver;
import org.e2e.labe2e01.utils.parameters.users.annotations.DriverWithVehicle;
import org.e2e.labe2e01.vehicle.domain.Vehicle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.ZonedDateTime;

@ExtendWith({UserParameterResolver.class})
class DriverTest {

    @Test
    void testDriverCreation(@BasicDriver Driver driver) {
        assertNotNull(driver);
        assertEquals(Role.DRIVER, driver.getRole());
        assertEquals("Jhon", driver.getFirstName());
        assertEquals("Doe", driver.getLastName());
        assertEquals("jhon.doe@example.com", driver.getEmail());
        assertEquals("password123", driver.getPassword());
        assertEquals("1234567890", driver.getPhoneNumber());
    }

    @Test
    void testGettersAndSetters(@DriverWithVehicle Driver driver) {
        driver.setId(100L);
        assertEquals(100L, driver.getId());

        driver.setAvgRating(4.5);
        assertEquals(4.5, driver.getAvgRating());

        driver.setRole(Role.DRIVER);
        assertEquals(Role.DRIVER, driver.getRole());

        driver.setTrips(10);
        assertEquals(10, driver.getTrips());

        ZonedDateTime now = ZonedDateTime.now();
        driver.setCreatedAt(now);
        assertEquals(now, driver.getCreatedAt());

        ZonedDateTime updated = now.plusDays(1);
        driver.setUpdatedAt(updated);
        assertEquals(updated, driver.getUpdatedAt());

        driver.setEmail("new.email@example.com");
        assertEquals("new.email@example.com", driver.getEmail());

        driver.setFirstName("Jane");
        assertEquals("Jane", driver.getFirstName());

        driver.setLastName("Smith");
        assertEquals("Smith", driver.getLastName());

        driver.setPassword("newpassword");
        assertEquals("newpassword", driver.getPassword());

        driver.setPhoneNumber("0987654321");
        assertEquals("0987654321", driver.getPhoneNumber());

        Coordinate coordinate = new Coordinate();
        driver.setCoordinate(coordinate);
        assertEquals(coordinate, driver.getCoordinate());

        driver.setCategory(Category.XL);
        assertEquals(Category.XL, driver.getCategory());
    }

    @Test
    public void testVehicleMethods(@DriverWithVehicle Driver driver) {
        Vehicle vehicle = driver.getVehicle();

        assertNotNull(vehicle);

        vehicle.setBrand("Honda");
        vehicle.setModel("Civic");
        vehicle.setLicensePlate("XYZ789");
        vehicle.setFabricationYear(2022);
        vehicle.setCapacity(4);

        driver.setVehicle(vehicle);

        assertEquals(vehicle, driver.getVehicle());
        assertEquals("Honda", driver.getVehicle().getBrand());
        assertEquals("Civic", driver.getVehicle().getModel());
        assertEquals("XYZ789", driver.getVehicle().getLicensePlate());
        assertEquals(2022, driver.getVehicle().getFabricationYear());
        assertEquals(4, driver.getVehicle().getCapacity());
    }
}

