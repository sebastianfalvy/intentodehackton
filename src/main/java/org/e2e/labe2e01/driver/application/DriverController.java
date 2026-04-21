package org.e2e.labe2e01.driver.application;


import lombok.RequiredArgsConstructor;
import org.e2e.labe2e01.driver.domain.Driver;
import org.e2e.labe2e01.driver.domain.DriverService;
import org.e2e.labe2e01.vehicle.domain.Vehicle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getDriver(@PathVariable Long id) {
        return driverService.findById(id)
                .map(driver -> ResponseEntity.ok().body(driver))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Driver> createDriver(@RequestBody Driver driver) {
        Driver savedDriver = driverService.save(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDriver);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Driver> updateDriver(@PathVariable Long id, @RequestBody Driver driver) {
        if (!driverService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        driver.setId(id);
        Driver updatedDriver = driverService.save(driver);
        return ResponseEntity.ok().body(updatedDriver);
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<Driver> updateLocation(
            @PathVariable Long id,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        Driver updatedDriver = driverService.updateLocation(id, latitude, longitude);
        return ResponseEntity.ok().body(updatedDriver);
    }

    @PatchMapping("/{id}/car")
    public ResponseEntity<Driver> addVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        Driver updatedDriver = driverService.addVehicle(id, vehicle);
        return ResponseEntity.ok().body(updatedDriver);
    }
}
