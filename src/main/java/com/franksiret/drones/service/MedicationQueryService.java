package com.franksiret.drones.service;

import com.franksiret.drones.domain.*; // for static metamodels
import com.franksiret.drones.domain.Medication;
import com.franksiret.drones.repository.MedicationRepository;
import com.franksiret.drones.service.criteria.MedicationCriteria;
import com.franksiret.drones.service.dto.MedicationDTO;
import com.franksiret.drones.service.mapper.MedicationMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Medication} entities in the database.
 * The main input is a {@link MedicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MedicationDTO} or a {@link Page} of {@link MedicationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicationQueryService extends QueryService<Medication> {

    private final Logger log = LoggerFactory.getLogger(MedicationQueryService.class);

    private final MedicationRepository medicationRepository;

    private final MedicationMapper medicationMapper;

    public MedicationQueryService(MedicationRepository medicationRepository, MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
    }

    /**
     * Return a {@link List} of {@link MedicationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MedicationDTO> findByCriteria(MedicationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Medication> specification = createSpecification(criteria);
        return medicationMapper.toDto(medicationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MedicationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicationDTO> findByCriteria(MedicationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medication> specification = createSpecification(criteria);
        return medicationRepository.findAll(specification, page).map(medicationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Medication> specification = createSpecification(criteria);
        return medicationRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medication> createSpecification(MedicationCriteria criteria) {
        Specification<Medication> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Medication_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Medication_.name));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), Medication_.weight));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), Medication_.code));
            }
            if (criteria.getDroneId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDroneId(), root -> root.join(Medication_.drone, JoinType.LEFT).get(Drone_.id))
                    );
            }
        }
        return specification;
    }
}
