package com.franksiret.drones.web.rest;

import com.franksiret.drones.repository.MedicationRepository;
import com.franksiret.drones.service.MedicationQueryService;
import com.franksiret.drones.service.MedicationService;
import com.franksiret.drones.service.criteria.MedicationCriteria;
import com.franksiret.drones.service.dto.MedicationDTO;
import com.franksiret.drones.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.franksiret.drones.domain.Medication}.
 */
@RestController
@RequestMapping("/api")
public class MedicationResource {

    private final Logger log = LoggerFactory.getLogger(MedicationResource.class);

    private static final String ENTITY_NAME = "medication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicationService medicationService;

    private final MedicationRepository medicationRepository;

    private final MedicationQueryService medicationQueryService;

    public MedicationResource(
        MedicationService medicationService,
        MedicationRepository medicationRepository,
        MedicationQueryService medicationQueryService
    ) {
        this.medicationService = medicationService;
        this.medicationRepository = medicationRepository;
        this.medicationQueryService = medicationQueryService;
    }

    /**
     * {@code POST  /medications} : Create a new medication.
     *
     * @param medicationDTO the medicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicationDTO, or with status {@code 400 (Bad Request)} if the medication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medications")
    public ResponseEntity<MedicationDTO> createMedication(@Valid @RequestBody MedicationDTO medicationDTO) throws URISyntaxException {
        log.debug("REST request to save Medication : {}", medicationDTO);
        if (medicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new medication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MedicationDTO result = medicationService.save(medicationDTO);
        return ResponseEntity
            .created(new URI("/api/medications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medications/:id} : Updates an existing medication.
     *
     * @param id the id of the medicationDTO to save.
     * @param medicationDTO the medicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicationDTO,
     * or with status {@code 400 (Bad Request)} if the medicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medications/{id}")
    public ResponseEntity<MedicationDTO> updateMedication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicationDTO medicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Medication : {}, {}", id, medicationDTO);
        if (medicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MedicationDTO result = medicationService.update(medicationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /medications/:id} : Partial updates given fields of an existing medication, field will ignore if it is null
     *
     * @param id the id of the medicationDTO to save.
     * @param medicationDTO the medicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicationDTO,
     * or with status {@code 400 (Bad Request)} if the medicationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/medications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedicationDTO> partialUpdateMedication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicationDTO medicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Medication partially : {}, {}", id, medicationDTO);
        if (medicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicationDTO> result = medicationService.partialUpdate(medicationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medicationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medications} : get all the medications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medications in body.
     */
    @GetMapping("/medications")
    public ResponseEntity<List<MedicationDTO>> getAllMedications(
        MedicationCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Medications by criteria: {}", criteria);
        Page<MedicationDTO> page = medicationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medications/count} : count all the medications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/medications/count")
    public ResponseEntity<Long> countMedications(MedicationCriteria criteria) {
        log.debug("REST request to count Medications by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medications/:id} : get the "id" medication.
     *
     * @param id the id of the medicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medications/{id}")
    public ResponseEntity<MedicationDTO> getMedication(@PathVariable Long id) {
        log.debug("REST request to get Medication : {}", id);
        Optional<MedicationDTO> medicationDTO = medicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicationDTO);
    }

    /**
     * {@code DELETE  /medications/:id} : delete the "id" medication.
     *
     * @param id the id of the medicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medications/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        log.debug("REST request to delete Medication : {}", id);
        medicationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
