package com.franksiret.drones.repository;

import com.franksiret.drones.domain.Medication;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Medication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long>, JpaSpecificationExecutor<Medication> {
    List<Medication> findAllByDroneId(Long id);

    @Modifying
    @Query(value = "delete from medication m where m.drone_id = :id", nativeQuery = true)
    void deleteAllByDroneId(@Param("id") Long id);

    @Modifying
    @Query(value = "delete from medication m where m.id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);
}
