package org.e2e.labe2e01.utils.parameters.vehicle.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface BasicVehicle {
}
