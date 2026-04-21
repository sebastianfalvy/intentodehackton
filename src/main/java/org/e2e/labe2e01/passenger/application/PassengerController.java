package org.e2e.labe2e01.passenger.application;

import lombok.RequiredArgsConstructor;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.passenger.domain.PassengerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @GetMapping("/{id}")
    public ResponseEntity<Passenger> getPassenger(@PathVariable Long id) {
        return passengerService.findById(id)
                .map(passenger -> ResponseEntity.ok().body(passenger))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Passenger> addPlace(
            @PathVariable Long id,
            @RequestBody Coordinate coordinate) {
        Passenger updatedPassenger = passengerService.addPlace(id, coordinate, "");
        return ResponseEntity.ok().body(updatedPassenger);
    }

    @GetMapping("/{id}/places")
    public ResponseEntity<List<Coordinate>> getPlaces(@PathVariable Long id) {
        List<Coordinate> places = passengerService.getPlaces(id);
        return ResponseEntity.ok().body(places);
    }

    @DeleteMapping("/{id}/places/{coordinateId}")
    public ResponseEntity<Void> removePlace(
            @PathVariable Long id,
            @PathVariable Long coordinateId) {
        passengerService.removePlace(id, coordinateId);
        return ResponseEntity.noContent().build();
    }
}
