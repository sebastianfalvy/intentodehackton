package org.e2e.labe2e01.ride.domain;

import lombok.RequiredArgsConstructor;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.driver.infrastructure.DriverRepository;
import org.e2e.labe2e01.ride.infrastructure.RideRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;

    public Ride createRide(Ride ride) {
        ride.setStatus(Status.REQUESTED);
        return rideRepository.save(ride);
    }

    public Ride assignDriver(Long rideId, Long driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + rideId));

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with id: " + driverId));

        ride.setDriver(driver);
        ride.setStatus(Status.ACCEPTED);
        return rideRepository.save(ride);
    }

    public void deleteRide(Long rideId) {
        rideRepository.deleteById(rideId);
    }

    public Page<Ride> getRidesByPassenger(Long passengerId, Pageable pageable) {
        return rideRepository.findByPassengerId(passengerId, pageable);
    }

    public Ride updateRideStatus(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + rideId));

        if (ride.getStatus() == Status.ACCEPTED) {
            ride.setStatus(Status.IN_PROGRESS);
        } else if (ride.getStatus() == Status.IN_PROGRESS) {
            ride.setStatus(Status.COMPLETED);
        }

        return rideRepository.save(ride);
    }

    public Optional<Ride> findById(Long rideId) {
        return rideRepository.findById(rideId);
    }
}
