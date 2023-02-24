package com.franksiret.drones.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.franksiret.drones.domain.Medication} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicationDTO implements Serializable {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    private String name;

    @NotNull
    @Min(value = 1)
    private Integer weight;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9_]+$")
    private String code;

    @Lob
    private byte[] image;

    private String imageContentType;

    private DroneDTO drone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public DroneDTO getDrone() {
        return drone;
    }

    public void setDrone(DroneDTO drone) {
        this.drone = drone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicationDTO)) {
            return false;
        }

        MedicationDTO medicationDTO = (MedicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", weight=" + getWeight() +
            ", code='" + getCode() + "'" +
            ", image='" + getImage() + "'" +
            ", drone=" + getDrone() +
            "}";
    }
}
