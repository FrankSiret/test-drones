package com.franksiret.drones.repository;

import com.franksiret.drones.domain.Drone;
import com.franksiret.drones.domain.enumeration.State;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Drone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DroneRepository extends JpaRepository<Drone, Long>, JpaSpecificationExecutor<Drone> {
    @Query(
        value = "select d from Drone d " +
        "left join Medication m on d = m.drone " +
        "where d.batteryCapacity >= 25 " +
        "group by d " +
        "having (d.weightLimit - sum(m.weight) >= :weight) or (sum(m.weight) is null and d.weightLimit >= :weight) "
    )
    List<Drone> findAllAvailableDroneByWeight(@Param("weight") Long weight);
}
