package org.e2e.labe2e01.vehicle.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class VehicleTest {

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setLicensePlate("ABC123");
        vehicle.setFabricationYear(2020);
        vehicle.setCapacity(5);
    }

    @Test
    void testVehicleCreation() {
        assertNotNull(vehicle);
        Assertions.assertEquals("Toyota", vehicle.getBrand());
        Assertions.assertEquals("Corolla", vehicle.getModel());
        Assertions.assertEquals("ABC123", vehicle.getLicensePlate());
        Assertions.assertEquals(2020, vehicle.getFabricationYear());
        Assertions.assertEquals(5, vehicle.getCapacity());
    }

    @Test
    void testVehicleBrand() {
        Assertions.assertEquals("Toyota", vehicle.getBrand());
        vehicle.setBrand("Volvo");
        Assertions.assertEquals("Volvo", vehicle.getBrand());
    }

    @Test
    void testVehicleModel() {
        Assertions.assertEquals("Corolla", vehicle.getModel());
        vehicle.setModel("Rav4");
        Assertions.assertEquals("Rav4", vehicle.getModel());
    }

    @Test
    void testVehicleLicensePlate() {
        Assertions.assertEquals("ABC123", vehicle.getLicensePlate());
        vehicle.setLicensePlate("DEF456");
        Assertions.assertEquals("DEF456", vehicle.getLicensePlate());
    }

    @Test
    void testVehicleYear() {
        Assertions.assertEquals(2020, vehicle.getFabricationYear());
        vehicle.setFabricationYear(2021);
        Assertions.assertEquals(2021, vehicle.getFabricationYear());
    }

    @Test
    void testVehicleCapacity() {
        Assertions.assertEquals(5, vehicle.getCapacity());
        vehicle.setCapacity(7);
        Assertions.assertEquals(7, vehicle.getCapacity());
    }
}