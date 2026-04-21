package org.e2e.labe2e01.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.e2e.labe2e01.driver.domain.Category;
import org.e2e.labe2e01.driver.domain.Driver;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Vehicle.class)
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, unique = true)
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column
    private Category category;

    @Column(nullable = false)
    private Integer fabricationYear;

    @Column(nullable = false)
    private Integer capacity;

    @OneToOne(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private Driver driver;
}