package com.franksiret.drones.service.criteria;

import com.franksiret.drones.domain.enumeration.Model;
import com.franksiret.drones.domain.enumeration.State;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.franksiret.drones.domain.Drone} entity. This class is used
 * in {@link com.franksiret.drones.web.rest.DroneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /drones?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DroneCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Model
     */
    public static class ModelFilter extends Filter<Model> {

        public ModelFilter() {}

        public ModelFilter(ModelFilter filter) {
            super(filter);
        }

        @Override
        public ModelFilter copy() {
            return new ModelFilter(this);
        }
    }

    /**
     * Class for filtering State
     */
    public static class StateFilter extends Filter<State> {

        public StateFilter() {}

        public StateFilter(StateFilter filter) {
            super(filter);
        }

        @Override
        public StateFilter copy() {
            return new StateFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter serialNumber;

    private ModelFilter model;

    private IntegerFilter weightLimit;

    private IntegerFilter batteryCapacity;

    private StateFilter state;

    private LongFilter medicationsId;

    private Boolean distinct;

    public DroneCriteria() {}

    public DroneCriteria(DroneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.serialNumber = other.serialNumber == null ? null : other.serialNumber.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.weightLimit = other.weightLimit == null ? null : other.weightLimit.copy();
        this.batteryCapacity = other.batteryCapacity == null ? null : other.batteryCapacity.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.medicationsId = other.medicationsId == null ? null : other.medicationsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DroneCriteria copy() {
        return new DroneCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            serialNumber = new StringFilter();
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ModelFilter getModel() {
        return model;
    }

    public ModelFilter model() {
        if (model == null) {
            model = new ModelFilter();
        }
        return model;
    }

    public void setModel(ModelFilter model) {
        this.model = model;
    }

    public IntegerFilter getWeightLimit() {
        return weightLimit;
    }

    public IntegerFilter weightLimit() {
        if (weightLimit == null) {
            weightLimit = new IntegerFilter();
        }
        return weightLimit;
    }

    public void setWeightLimit(IntegerFilter weightLimit) {
        this.weightLimit = weightLimit;
    }

    public IntegerFilter getBatteryCapacity() {
        return batteryCapacity;
    }

    public IntegerFilter batteryCapacity() {
        if (batteryCapacity == null) {
            batteryCapacity = new IntegerFilter();
        }
        return batteryCapacity;
    }

    public void setBatteryCapacity(IntegerFilter batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public StateFilter getState() {
        return state;
    }

    public StateFilter state() {
        if (state == null) {
            state = new StateFilter();
        }
        return state;
    }

    public void setState(StateFilter state) {
        this.state = state;
    }

    public LongFilter getMedicationsId() {
        return medicationsId;
    }

    public LongFilter medicationsId() {
        if (medicationsId == null) {
            medicationsId = new LongFilter();
        }
        return medicationsId;
    }

    public void setMedicationsId(LongFilter medicationsId) {
        this.medicationsId = medicationsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DroneCriteria that = (DroneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(model, that.model) &&
            Objects.equals(weightLimit, that.weightLimit) &&
            Objects.equals(batteryCapacity, that.batteryCapacity) &&
            Objects.equals(state, that.state) &&
            Objects.equals(medicationsId, that.medicationsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serialNumber, model, weightLimit, batteryCapacity, state, medicationsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DroneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (serialNumber != null ? "serialNumber=" + serialNumber + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (weightLimit != null ? "weightLimit=" + weightLimit + ", " : "") +
            (batteryCapacity != null ? "batteryCapacity=" + batteryCapacity + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (medicationsId != null ? "medicationsId=" + medicationsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
