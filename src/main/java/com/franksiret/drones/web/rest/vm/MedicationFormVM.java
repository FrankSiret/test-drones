package com.franksiret.drones.web.rest.vm;

import javax.validation.constraints.*;

/**
 * View Model object for load drone's Medication.
 */
public class MedicationFormVM {

    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    private String name;

    @NotNull
    @Min(value = 0)
    private Integer weight;

    @NotNull
    @Pattern(regexp = "^[A-Z0-9_]+$")
    private String code;

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

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicationFormVM{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", weight=" + getWeight() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
