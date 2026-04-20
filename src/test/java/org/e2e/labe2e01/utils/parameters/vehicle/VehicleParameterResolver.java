package org.e2e.labe2e01.utils.parameters.vehicle;

import org.e2e.labe2e01.driver.domain.Category;
import org.e2e.labe2e01.utils.parameters.vehicle.annotations.BasicVehicle;
import org.e2e.labe2e01.utils.parameters.vehicle.annotations.PersistedVehicle;
import org.e2e.labe2e01.utils.parameters.vehicle.annotations.PersistedVehicleSet;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.e2e.labe2e01.vehicle.infrastructure.VehicleRepository;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class VehicleParameterResolver implements ParameterResolver, BeforeEachCallback {
    private VehicleRepository vehicleRepo;

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        ApplicationContext ctx = SpringExtension.getApplicationContext(extensionContext);

        try {
            vehicleRepo = ctx.getBean(VehicleRepository.class);
        } catch (NoSuchBeanDefinitionException ex) {
            // Ignore if the test does not load any context
        }

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        return parameter.isAnnotationPresent(BasicVehicle.class)
                || parameter.isAnnotationPresent(PersistedVehicle.class)
                || parameter.isAnnotationPresent(PersistedVehicleSet.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();

        if (parameter.isAnnotationPresent(BasicVehicle.class)) {
            return createBasicVehicle();
        }

        if (parameter.isAnnotationPresent(PersistedVehicle.class)) {
            Vehicle vehicle = createBasicVehicle();
            if (vehicleRepo == null) {
                throw new ParameterResolutionException("Cannot persist vehicle because VehicleRepository is null. Ensure the test is loading the Spring context.");
            }
            return vehicleRepo.save(vehicle);
        }

        if (parameter.isAnnotationPresent(PersistedVehicleSet.class)) {
            PersistedVehicleSet annotation = parameter.getAnnotation(PersistedVehicleSet.class);
            int count = annotation.count();
            String brand = annotation.brand();

            return createPersistedVehicleSet(count, brand);
        }

        throw new ParameterResolutionException("No handler for parameter " + parameter.getName());
    }

    private Vehicle createBasicVehicle() {
        Vehicle newVehicle = new Vehicle();
        newVehicle.setBrand("Toyota");
        newVehicle.setModel("Corolla");
        newVehicle.setLicensePlate("ABC-1234");
        newVehicle.setCapacity(5);
        newVehicle.setFabricationYear(2000);

        return newVehicle;
    }

    private List<Vehicle> createPersistedVehicleSet(int count, String brand) {
        if (vehicleRepo == null) {
            throw new ParameterResolutionException("Cannot persist vehicle because VehicleRepository is null. Ensure the test is loading the Spring context.");
        }

        List<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Vehicle newVehicle = createBasicVehicle();
            newVehicle.setBrand(brand);
            newVehicle.setLicensePlate(brand + Integer.valueOf(i).toString());

            vehicles.add(vehicleRepo.save(newVehicle));
        }

        return vehicles;
    }
}
