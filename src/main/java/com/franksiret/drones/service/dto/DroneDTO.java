package com.franksiret.drones.service.dto;

import com.franksiret.drones.domain.enumeration.Model;
import com.franksiret.drones.domain.enumeration.State;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.franksiret.drones.domain.Drone} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DroneDTO implements Serializable {

    private Long id;

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

    @NotNull
    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DroneDTO)) {
            return false;
        }

        DroneDTO droneDTO = (DroneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, droneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DroneDTO{" +
            "id=" + getId() +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", model='" + getModel() + "'" +
            ", weightLimit=" + getWeightLimit() +
            ", batteryCapacity=" + getBatteryCapacity() +
            ", state='" + getState() + "'" +
            "}";
    }
}
