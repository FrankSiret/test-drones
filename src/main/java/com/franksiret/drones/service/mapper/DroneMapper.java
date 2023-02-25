package com.franksiret.drones.service.mapper;

import com.franksiret.drones.domain.Drone;
import com.franksiret.drones.domain.enumeration.State;
import com.franksiret.drones.service.dto.DroneDTO;
import com.franksiret.drones.web.rest.vm.DroneVM;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Drone} and its DTO {@link DroneDTO}.
 */
@Mapper(componentModel = "spring", imports = { State.class })
public interface DroneMapper extends EntityMapper<DroneDTO, Drone> {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "state", expression = "java(State.IDLE)")
    DroneDTO toDto(DroneVM droneVM);
}
