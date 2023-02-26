package com.franksiret.drones.web.rest.vm;

import com.franksiret.drones.domain.enumeration.Model;
import com.franksiret.drones.domain.enumeration.State;
import com.franksiret.drones.service.dto.DroneDTO;
import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * View Model object for request post Drone.
 */
public class DroneVM implements Serializable {

    @NotNull
    @Size(max = 100)
    @Pattern(regexp = "^\\d+$")
    private String serialNumber;

    @NotNull
    private Model model;

    @NotNull
    @Min(value = 0)
    @Max(value = 500)
    private Integer weightLimit;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private Integer batteryCapacity;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Integer getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(Integer weightLimit) {
        this.weightLimit = weightLimit;
    }

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(Integer batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DroneVM{" +
            "serialNumber='" + getSerialNumber() + "'" +
            ", model='" + getModel() + "'" +
            ", weightLimit=" + getWeightLimit() +
            "}";
    }

    public DroneDTO toDto() {
        DroneDTO dto = new DroneDTO();
        dto.setSerialNumber(serialNumber);
        dto.setModel(model);
        dto.setWeightLimit(weightLimit);
        dto.setBatteryCapacity(batteryCapacity);
        dto.setState(State.IDLE);
        return dto;
    }
}
