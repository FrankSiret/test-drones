package com.franksiret.drones.service;

import com.franksiret.drones.domain.Drone;
import com.franksiret.drones.repository.DroneRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Schedule {

    private final Logger log = LoggerFactory.getLogger(Schedule.class);

    private final DroneRepository droneRepository;

    public Schedule(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    // every 5 min
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void auditDroneBattery() {
        List<Drone> drones = droneRepository.findAll();
        for (Drone drone : drones) {
            log.info(
                "The drone with id '{}' and serial number '{}' has {}% of battery level",
                drone.getId(),
                drone.getSerialNumber(),
                drone.getBatteryCapacity()
            );
        }
    }
}
