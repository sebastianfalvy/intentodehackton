package org.e2e.labe2e01.utils.parameters.users.annotations;

import org.e2e.labe2e01.vehicle.domain.Vehicle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface DriverWithVehicle {
}
