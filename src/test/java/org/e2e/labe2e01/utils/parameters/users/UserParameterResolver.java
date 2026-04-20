package org.e2e.labe2e01.utils.parameters.users;

import org.e2e.labe2e01.coordinate.domain.Coordinate;
import org.e2e.labe2e01.coordinate.infrastructure.CoordinateRepository;
import org.e2e.labe2e01.driver.domain.Category;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.driver.infrastructure.DriverRepository;
import org.e2e.labe2e01.passenger.domain.Passenger;
import org.e2e.labe2e01.passenger.infrastructure.PassengerRepository;
import org.e2e.labe2e01.user.domain.Role;
import org.e2e.labe2e01.utils.parameters.users.annotations.*;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Parameter;
import java.time.ZonedDateTime;

public class UserParameterResolver implements ParameterResolver, BeforeEachCallback {
    private DriverRepository driverRepo;
    private PassengerRepository passengerRepo;
    private CoordinateRepository coordinateRepo;

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        ApplicationContext ctx = SpringExtension.getApplicationContext(extensionContext);

        try {
            driverRepo = ctx.getBean(DriverRepository.class);
            passengerRepo = ctx.getBean(PassengerRepository.class);
            coordinateRepo = ctx.getBean(CoordinateRepository.class);
        } catch (NoSuchBeanDefinitionException ex) {
            // Ignore if the test does not load any context
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return parameter.isAnnotationPresent(BasicDriver.class)
                || parameter.isAnnotationPresent(DriverWithVehicle.class)
                || parameter.isAnnotationPresent(PersistedDriver.class)
                || parameter.isAnnotationPresent(PersistedPassenger.class)
                || parameter.isAnnotationPresent(PersistedPassengerWithPlaces.class)
                || parameter.isAnnotationPresent(BasicPassenger.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();

        if (parameter.isAnnotationPresent(BasicDriver.class)) {
            return createBasicDriver();
        }

        if (parameter.isAnnotationPresent(DriverWithVehicle.class)) {
            return createDriverWithVehicle();
        }

        if (parameter.isAnnotationPresent(PersistedDriver.class)) {
            return createPersistedDriver();
        }

        if (parameter.isAnnotationPresent(PersistedPassenger.class)) {
            return createPersistedPassenger();
        }

        if (parameter.isAnnotationPresent(PersistedPassengerWithPlaces.class)) {
            return createPersistedPassengerWithPlaces();
        }

        if (parameter.isAnnotationPresent(BasicPassenger.class)){
            return createBasicPassenger();
        }

        throw new ParameterResolutionException("No handler for parameter " + parameter.getName());
    }

    public Driver createBasicDriver() {
        Driver driver = new Driver();
        driver.setRole(Role.DRIVER);
        driver.setCategory(Category.X);
        driver.setFirstName("Jhon");
        driver.setLastName("Doe");
        driver.setEmail("jhon.doe@example.com");
        driver.setPassword("password123");
        driver.setPhoneNumber("1234567890");
        driver.setCreatedAt(ZonedDateTime.now());

        Coordinate coordinate = new Coordinate(40.7128, -74.0060);
        driver.setCoordinate(coordinate);
        return driver;
    }

    public Driver createDriverWithVehicle() {
        Driver driver = createBasicDriver();

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setLicensePlate("ABC123");
        vehicle.setFabricationYear(2020);
        vehicle.setCapacity(5);
        driver.setVehicle(vehicle);
        return driver;
    }

    public Driver createPersistedDriver() {
        Driver driver = createDriverWithVehicle();
        return driverRepo.save(driver);
    }

    private Passenger createPersistedPassenger() {
        Passenger passenger = new Passenger();
        passenger.setRole(Role.PASSENGER);
        passenger.setFirstName("Joe");
        passenger.setLastName("Foe");
        passenger.setEmail("joe.foe@passenger.com");
        passenger.setPassword("password123");
        passenger.setPhoneNumber("0987654321");
        passenger.setCreatedAt(ZonedDateTime.now());

        return passengerRepo.save(passenger);
    }

    private Passenger createPersistedPassengerWithPlaces() {
        if (passengerRepo == null || coordinateRepo == null) {
            throw new IllegalStateException("Repositories not initialized. Ensure that the test context is loaded.");
        }
        Passenger existingPassenger = passengerRepo.findAll().stream().findFirst().orElse(null);
        Passenger passenger;

        if (existingPassenger != null) passenger = existingPassenger;
        else passenger = createPersistedPassenger();

        Coordinate place1 = new Coordinate(34.0522, -118.2437);
        Coordinate place2 = new Coordinate(41.8781, -87.6298);
        coordinateRepo.save(place1);
        coordinateRepo.save(place2);

        passenger.addPlace(place1, "Home");
        passenger.addPlace(place2, "Work");

        return passengerRepo.save(passenger);
    }

    private Passenger createBasicPassenger() {
        Passenger passenger = new Passenger();
        passenger.setRole(Role.PASSENGER);
        passenger.setFirstName("Joe");
        passenger.setLastName("Foe");
        passenger.setEmail("joe.foe@passenger.com");
        passenger.setPassword("password123");
        passenger.setPhoneNumber("0987654321");
        passenger.setCreatedAt(ZonedDateTime.now());

        return passenger;
    }
}

