package org.e2e.labe2e01.driver.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.coordinate.infrastructure.CoordinateRepository;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.driver.infrastructure.DriverRepository;
import org.e2e.labe2e01.utils.Reader;
import org.e2e.labe2e01.utils.parameters.users.UserParameterResolver;
import org.e2e.labe2e01.utils.parameters.users.annotations.PersistedDriver;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.e2e.labe2e01.vehicle.infrastructure.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith({UserParameterResolver.class})
public class DriverControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Reader reader;

    @Test
    public void should200WhenGetDriver(@PersistedDriver Driver driver) throws Exception {
        /// Act & Assert
        mockMvc.perform(get("/driver/{id}", driver.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(driver.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(driver.getLastName()))
                .andExpect(jsonPath("$.email").value(driver.getEmail()))
                .andExpect(jsonPath("$.category").value(driver.getCategory().toString()));
    }

    @Test
    public void should201whenPostDriver() throws Exception {
        String payload = Reader.readJsonFile("requests/driver/createDriver.json");

        mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        List<Driver> drivers = driverRepository.findAll();
        assertEquals(1, drivers.size());
        assertEquals(drivers.get(0).getFirstName(), reader.getStringValue(payload, "firstName"));
        assertEquals(drivers.get(0).getLastName(), reader.getStringValue(payload, "lastName"));
    }

    @Test
    public void should204WhenDeleteDriver(@PersistedDriver Driver driver) throws Exception {
        ///  Act & Assert
        mockMvc.perform(delete("/driver/{id}", driver.getId()))
                .andExpect(status().isNoContent());

        assertFalse(driverRepository.existsById(driver.getId()));
    }

    @Test
    public void should200WhenUpdateDriver(@PersistedDriver Driver driver) throws Exception {
        driver.setFirstName("UpdatedFirstName");
        driver.setLastName("UpdatedLastName");

        String payload = objectMapper.writeValueAsString(driver);

        mockMvc.perform(put("/driver/{id}", driver.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        assertEquals("UpdatedFirstName", updatedDriver.getFirstName());
        assertEquals("UpdatedLastName", updatedDriver.getLastName());
    }

    @Test
    public void testUpdateDriverLocation(@PersistedDriver Driver driver) throws Exception {
        // Arrange
        Long coordinateCountBefore = coordinateRepository.count();

        // Act & Assert
        mockMvc.perform(patch("/driver/{id}/location", driver.getId())
                        .param("latitude", "42.123")
                        .param("longitude", "-71.987"))
                .andExpect(status().isOk());

        // Assert
        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        assertEquals(42.123, updatedDriver.getCoordinate().getLatitude());
        assertEquals(-71.987, updatedDriver.getCoordinate().getLongitude());

        List<Coordinate> allCoordinates = coordinateRepository.findAll();

        Coordinate updatedCoordinate = coordinateRepository
                .findByLatitudeAndLongitude(42.123, -71.987)
                .orElseThrow();

        assertNotNull(updatedCoordinate);
        assertEquals(coordinateCountBefore + 1, allCoordinates.size());
    }

    @Test
    public void testUpdateDriverCar(@PersistedDriver Driver driver) throws Exception {

        // Arrange
        String payload = Reader.readJsonFile("requests/driver/newVehicle.json");

        // Act & Assert
        mockMvc.perform(patch("/driver/{id}/car", driver.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());

        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();

        // Assert
        assertEquals("Honda", updatedDriver.getVehicle().getBrand());
        assertEquals("Accord", updatedDriver.getVehicle().getModel());
        assertEquals("XYZ123", updatedDriver.getVehicle().getLicensePlate());

        List<Vehicle> allVehicles = vehicleRepository.findAll();
        assertEquals(  2, allVehicles.size());
    }
}
