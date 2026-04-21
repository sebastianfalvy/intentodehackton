package org.e2e.labe2e01.ride.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.passenger.domain.Passenger;

import java.time.ZonedDateTime;

@Entity
@Table(name = "rides")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Ride.class)
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String destinationName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_coordinate_id", nullable = false)
    private Coordinate originCoordinates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_coordinate_id", nullable = false)
    private Coordinate destinationCoordinates;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private ZonedDateTime departureDate;

    @Column
    private ZonedDateTime arrivalDate;
}
