package com.franksiret.drones.web.rest.vm;

import com.franksiret.drones.domain.enumeration.State;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * View Model object for request post Drone.
 */
public class DroneUpdateVM implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private Integer batteryCapacity;

    @NotNull
    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(Integer batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DroneUpdateVM{" +
            "batteryCapacity='" + getBatteryCapacity() + "'" +
            "state=" + getState() +
            "}";
    }
}
