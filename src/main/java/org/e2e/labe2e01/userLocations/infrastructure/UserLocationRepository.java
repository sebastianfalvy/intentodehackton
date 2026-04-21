package org.e2e.labe2e01.userLocations.infrastructure;

import org.e2e.labe2e01.userLocations.domain.UserLocation;
import org.e2e.labe2e01.userLocations.domain.PassengerCoordinateId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, PassengerCoordinateId> {
}

