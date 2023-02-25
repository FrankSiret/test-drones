package com.franksiret.drones.service.mapper;

import com.franksiret.drones.domain.Drone;
import com.franksiret.drones.domain.Medication;
import com.franksiret.drones.service.dto.DroneDTO;
import com.franksiret.drones.service.dto.MedicationDTO;
import com.franksiret.drones.web.rest.vm.MedicationFormVM;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medication} and its DTO {@link MedicationDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedicationMapper extends EntityMapper<MedicationDTO, Medication> {
    @Mapping(target = "image", ignore = true)
    MedicationDTO toDto(MedicationFormVM s);

    @Mapping(target = "drone", source = "drone", qualifiedByName = "droneId")
    MedicationDTO toDto(Medication s);

    @Named("droneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DroneDTO toDtoDroneId(Drone drone);
}
