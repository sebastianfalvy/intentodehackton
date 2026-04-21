package org.e2e.labe2e01.ride.application;


import lombok.RequiredArgsConstructor;
import org.e2e.labe2e01.ride.domain.Ride;
import org.e2e.labe2e01.ride.domain.RideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;

    @PostMapping
    public ResponseEntity<Ride> createRide(@RequestBody Ride ride) {
        Ride createdRide = rideService.createRide(ride);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRide);
    }

    @PatchMapping("/{rideId}/assign/{driverId}")
    public ResponseEntity<Ride> assignDriver(
            @PathVariable Long rideId,
            @PathVariable Long driverId) {
        Ride updatedRide = rideService.assignDriver(rideId, driverId);
        return ResponseEntity.ok().body(updatedRide);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        rideService.deleteRide(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{passengerId}")
    public ResponseEntity<Page<Ride>> getRidesByPassenger(
            @PathVariable Long passengerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        Page<Ride> rides = rideService.getRidesByPassenger(passengerId, pageable);
        return ResponseEntity.ok().body(rides);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Ride> updateRideStatus(@PathVariable Long id) {
        Ride updatedRide = rideService.updateRideStatus(id);
        return ResponseEntity.ok().body(updatedRide);
    }
}
