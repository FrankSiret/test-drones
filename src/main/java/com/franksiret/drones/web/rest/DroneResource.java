package com.franksiret.drones.web.rest;

import com.franksiret.drones.domain.Drone;
import com.franksiret.drones.domain.Medication;
import com.franksiret.drones.repository.DroneRepository;
import com.franksiret.drones.repository.MedicationRepository;
import com.franksiret.drones.service.DroneQueryService;
import com.franksiret.drones.service.DroneService;
import com.franksiret.drones.service.ToHeavyException;
import com.franksiret.drones.service.criteria.DroneCriteria;
import com.franksiret.drones.service.dto.DroneDTO;
import com.franksiret.drones.service.dto.MedicationDTO;
import com.franksiret.drones.service.mapper.MedicationMapper;
import com.franksiret.drones.web.rest.errors.BadRequestAlertException;
import com.franksiret.drones.web.rest.vm.MedicationFormVM;
import java.io.IOException;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.franksiret.drones.domain.Drone}.
 */
@RestController
@RequestMapping("/api")
public class DroneResource {

    private final Logger log = LoggerFactory.getLogger(DroneResource.class);

    private static final String ENTITY_NAME = "drone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DroneService droneService;

    private final DroneRepository droneRepository;

    private final DroneQueryService droneQueryService;

    private final MedicationMapper medicationMapper;

    private final MedicationRepository medicationRepository;

    public DroneResource(
        DroneService droneService,
        DroneRepository droneRepository,
        DroneQueryService droneQueryService,
        MedicationMapper medicationMapper,
        MedicationRepository medicationRepository
    ) {
        this.droneService = droneService;
        this.droneRepository = droneRepository;
        this.droneQueryService = droneQueryService;
        this.medicationMapper = medicationMapper;
        this.medicationRepository = medicationRepository;
    }

    /**
     * {@code POST  /drones} : Create a new drone.
     *
     * @param droneDTO the droneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new droneDTO, or with status {@code 400 (Bad Request)} if the drone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/drones")
    public ResponseEntity<DroneDTO> createDrone(@Valid @RequestBody DroneDTO droneDTO) throws URISyntaxException {
        log.debug("REST request to save Drone : {}", droneDTO);
        if (droneDTO.getId() != null) {
            throw new BadRequestAlertException("A new drone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DroneDTO result = droneService.save(droneDTO);
        return ResponseEntity
            .created(new URI("/api/drones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /drones/:id} : Updates an existing drone.
     *
     * @param id the id of the droneDTO to save.
     * @param droneDTO the droneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated droneDTO,
     * or with status {@code 400 (Bad Request)} if the droneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the droneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/drones/{id}")
    public ResponseEntity<DroneDTO> updateDrone(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DroneDTO droneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Drone : {}, {}", id, droneDTO);
        if (droneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, droneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!droneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DroneDTO result = droneService.update(droneDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, droneDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /drones/:id} : Partial updates given fields of an existing drone, field will ignore if it is null
     *
     * @param id the id of the droneDTO to save.
     * @param droneDTO the droneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated droneDTO,
     * or with status {@code 400 (Bad Request)} if the droneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the droneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the droneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/drones/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DroneDTO> partialUpdateDrone(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DroneDTO droneDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Drone partially : {}, {}", id, droneDTO);
        if (droneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, droneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!droneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DroneDTO> result = droneService.partialUpdate(droneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, droneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /drones} : get all the drones.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of drones in body.
     */
    @GetMapping("/drones")
    public ResponseEntity<List<DroneDTO>> getAllDrones(
        DroneCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Drones by criteria: {}", criteria);
        Page<DroneDTO> page = droneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /drones/count} : count all the drones.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/drones/count")
    public ResponseEntity<Long> countDrones(DroneCriteria criteria) {
        log.debug("REST request to count Drones by criteria: {}", criteria);
        return ResponseEntity.ok().body(droneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /drones/:id} : get the "id" drone.
     *
     * @param id the id of the droneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the droneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/drones/{id}")
    public ResponseEntity<DroneDTO> getDrone(@PathVariable Long id) {
        log.debug("REST request to get Drone : {}", id);
        Optional<DroneDTO> droneDTO = droneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(droneDTO);
    }

    /**
     * {@code DELETE  /drones/:id} : delete the "id" drone.
     *
     * @param id the id of the droneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/drones/{id}")
    public ResponseEntity<Void> deleteDrone(@PathVariable Long id) {
        log.debug("REST request to delete Drone : {}", id);
        droneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code PATCH  /drones/:id/bulk-load} : Loading a drone with a collections of medication items.
     *
     * @param id the id of the droneDTO to load medication items.
     * @param medicationDTOs the list of medication items to load.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated droneDTO,
     * or with status {@code 400 (Bad Request)} if the droneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the droneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping("/drones/{id}/bulk-load")
    public ResponseEntity<Drone> bulkLoadDrone(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody List<MedicationDTO> medicationDTOs
    ) throws URISyntaxException {
        log.debug("REST request to load to a Drone medication items: {}, {}", id, medicationDTOs);
        if (!droneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        for (MedicationDTO medicationDTO : medicationDTOs) {
            validateMedicationOrThrow(medicationDTO);
        }
        Drone drone = droneRepository.findById(id).get();
        if (!checkWeight(drone, medicationDTOs)) {
            throw new ToHeavyException("The drone cannot be loaded with more weight that it can carry");
        }
        List<Medication> medications = medicationMapper.toEntity(medicationDTOs);
        for (Medication medication : medications) {
            drone.addMedications(medication);
        }
        Drone result = droneService.saveDroneMedications(drone);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /drones/:id/load} : Loading a drone with a medication item.
     *
     * @param id the id of the droneDTO to load medication items.
     * @param medicationDTO the list of medication items to load.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated droneDTO,
     * or with status {@code 400 (Bad Request)} if the droneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the droneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws IOException
     */
    @PatchMapping(path = "/drones/{id}/load", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Drone> loadDrone(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestPart("medication") @Valid MedicationFormVM medicationFormVM,
        @RequestPart(value = "image", required = false) MultipartFile image
    ) throws URISyntaxException, IOException {
        log.debug("REST request to load to a Drone one medication item: {}, {}", id, medicationFormVM);
        if (!droneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        if (medicationFormVM.getId() != null) {
            throw new BadRequestAlertException("A new load medication item cannot have an ID", ENTITY_NAME, "idmedicationexists");
        }
        Drone drone = droneRepository.findById(id).get();
        MedicationDTO medicationDTO = medicationMapper.toDto(medicationFormVM);
        if (image != null && !image.isEmpty()) {
            medicationDTO.setImage(image.getBytes());
            medicationDTO.setImageContentType(image.getContentType());
        }
        validateMedicationOrThrow(medicationDTO);
        if (!checkWeight(drone, List.of(medicationDTO))) {
            throw new ToHeavyException("The drone cannot be loaded with more weight that it can carry");
        }
        Medication medication = medicationMapper.toEntity(medicationDTO);
        drone.addMedications(medication);
        Drone result = droneService.saveDroneMedications(drone);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private boolean checkWeight(Drone drone, List<MedicationDTO> medicationDTOs) {
        if (drone.getMedications() == null) {
            return true;
        }
        Integer weightLimit = drone.getWeightLimit();
        Integer currentWeight = drone.getMedications().stream().mapToInt(Medication::getWeight).sum();
        Integer weightToLoad = medicationDTOs.stream().mapToInt(MedicationDTO::getWeight).sum();
        return currentWeight + weightToLoad <= weightLimit;
    }

    private boolean validateMedicationOrThrow(MedicationDTO medicationDTO) {
        if (medicationDTO.getId() != null) {
            throw new BadRequestAlertException("New Medication item cannot have an ID", ENTITY_NAME, "idmedicationexists");
        }
        if ((medicationDTO.getImageContentType() != null) ^ (medicationDTO.getImage() != null)) {
            throw new BadRequestAlertException(
                "Medication item should have either both (image and imageContentType) or neither",
                ENTITY_NAME,
                "imagemedicationerror"
            );
        }
        if (medicationDTO.getImageContentType() != null && !medicationDTO.getImageContentType().startsWith("image/")) {
            throw new BadRequestAlertException(
                "Invalid imageContentType in the Medication item, only image/* type are available",
                ENTITY_NAME,
                "imagemedicationerror"
            );
        }
        if (medicationDTO.getImage() != null && medicationDTO.getImage().length > 1024 * 1024) {
            throw new BadRequestAlertException(
                "Image is too large in the Medication item, should have at most 1MB",
                ENTITY_NAME,
                "imagemedicationerror"
            );
        }
        return true;
    }

    /**
     * {@code GET  /drones/:id/medications} : get all load of the "id" drone.
     *
     * @param id the id of the droneDTO to retrieve its loads.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the droneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/drones/{id}/medications")
    public ResponseEntity<List<MedicationDTO>> getDroneLoad(@PathVariable Long id) {
        log.debug("REST request to get all Drone loaded medication items : {}", id);
        if (!droneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        List<Medication> medications = medicationRepository.findAllByDroneId(id);
        List<MedicationDTO> medicationDTOs = medicationMapper.toDto(medications);
        return ResponseEntity.ok().body(medicationDTOs);
    }
}
