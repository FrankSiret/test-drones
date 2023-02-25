package com.franksiret.drones.web.rest.vm;

import java.io.Serializable;

/**
 * View Model object for response post Drone battery level.
 */
public class DroneBatteryVM implements Serializable {

    private Integer batteryLevel;

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DroneBatteryVM{" +
            "batteryLevel='" + getBatteryLevel() + "'" +
            "}";
    }
}
