package org.e2e.labe2e01.passenger.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.coordinate.infrastructure.CoordinateRepository;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.passenger.infrastructure.PassengerRepository;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.utils.parameters.users.UserParameterResolver;
import org.e2e.labe2e01.utils.parameters.users.annotations.PersistedPassenger;
import org.e2e.labe2e01.utils.parameters.users.annotations.PersistedPassengerWithPlaces;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith({UserParameterResolver.class})
public class PassengerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetPassengerById(@PersistedPassenger Passenger passenger) throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/passenger/{id}", passenger.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(passenger.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(passenger.getFirstName())));

    }

    @Test
    public void testDeletePassenger(@PersistedPassenger Passenger passenger) throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/passenger/{id}", passenger.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertFalse(passengerRepository.findById(passenger.getId()).isPresent());
    }

    @Test
    public void testAddPlace(@PersistedPassenger Passenger passenger) throws Exception {
        // Arrange
        Coordinate newCoordinate = new Coordinate();
        newCoordinate.setLatitude(42.1234);
        newCoordinate.setLongitude(-71.9876);

        String payload = objectMapper.writeValueAsString(newCoordinate);

        // Act & Assert
        mockMvc.perform(patch("/passenger/{id}", passenger.getId())
                        .param("description", "Sample Place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeletePlace(@PersistedPassengerWithPlaces @NotNull Passenger passenger) throws Exception {
        // Arrange
        Coordinate currentCoordinate = passenger.getPlaces().get(0).getCoordinate();
        int initialSize = passenger.getPlaces().size();

        // Act & Assert
        mockMvc.perform(delete("/passenger/{id}/places/{coordinateId}", passenger.getId(), currentCoordinate.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).orElseThrow();
        int updatedSize = updatedPassenger.getPlaces().size();
        Assertions.assertEquals(initialSize - 1, updatedSize);
    }

    @Test
    public void testGetPlaces(@PersistedPassengerWithPlaces Passenger passenger) throws Exception {
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/passenger/{id}/places", passenger.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }
}
