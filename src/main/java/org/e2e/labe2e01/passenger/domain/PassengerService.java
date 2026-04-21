package org.e2e.labe2e01.passenger.domain;

import lombok.RequiredArgsConstructor;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.coordinate.infrastructure.CoordinateRepository;
import org.e2e.labe2e01.passenger.infrastructure.PassengerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private final CoordinateRepository coordinateRepository;

    public Optional<Passenger> findById(Long id) {
        return passengerRepository.findById(id);
    }

    public void deleteById(Long id) {
        passengerRepository.deleteById(id);
    }

    public Passenger addPlace(Long passengerId, Coordinate coordinate, String description) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found with id: " + passengerId));
        
        Coordinate savedCoordinate = coordinateRepository.save(coordinate);
        passenger.addPlace(savedCoordinate, description);
        return passengerRepository.save(passenger);
    }

    public List<Coordinate> getPlaces(Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found with id: " + passengerId));
        return passenger.getPlacesList();
    }

    public void removePlace(Long passengerId, Long coordinateId) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found with id: " + passengerId));
        
        Coordinate coordinate = coordinateRepository.findById(coordinateId)
                .orElseThrow(() -> new RuntimeException("Coordinate not found with id: " + coordinateId));
        
        passenger.removePlace(coordinate);
        passengerRepository.save(passenger);
    }
}
