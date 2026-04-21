package org.e2e.labe2e01.userLocations.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.passenger.domain.Passenger;

@Entity
@Table(name = "user_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = UserLocation.class)
public class UserLocation {
    @EmbeddedId
    private PassengerCoordinateId id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("passengerId")
    private Passenger passenger;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("coordinateId")
    private Coordinate coordinate;

    @Column
    private String description;

    public UserLocation(Passenger passenger, Coordinate coordinate, String description) {
        this.passenger = passenger;
        this.coordinate = coordinate;
        this.description = description;
        this.id = new PassengerCoordinateId(passenger.getId(), coordinate.getId());
    }
}
