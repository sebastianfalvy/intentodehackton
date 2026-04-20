package org.e2e.labe2e01.review.domain;

import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.ride.domain.Ride;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.utils.parameters.users.UserParameterResolver;
import org.e2e.labe2e01.utils.parameters.users.annotations.BasicDriver;
import org.e2e.labe2e01.utils.parameters.users.annotations.BasicPassenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.ZonedDateTime;

@ExtendWith({UserParameterResolver.class})
public class ReviewTest {
    private Review review;

    @Test
    void testReviewCreation(@BasicPassenger Passenger passenger, @BasicDriver Driver driver) {
        // Arrange & Act
        review = new Review();
        review.setComment("comment1");
        review.setRating(5);
        review.setAuthor(passenger);
        review.setTarget(driver);

        Ride ride = new Ride();
        review.setRide(ride);

        // Assert
        Assertions.assertEquals("comment1", review.getComment());
        Assertions.assertEquals(5, review.getRating());
        Assertions.assertEquals(passenger, review.getAuthor());
        Assertions.assertEquals(driver, review.getTarget());
        Assertions.assertEquals(ride, review.getRide());
    }
}
