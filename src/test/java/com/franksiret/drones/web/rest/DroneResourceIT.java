package com.franksiret.drones.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.franksiret.drones.IntegrationTest;
import com.franksiret.drones.domain.Drone;
import com.franksiret.drones.domain.Medication;
import com.franksiret.drones.domain.enumeration.Model;
import com.franksiret.drones.domain.enumeration.State;
import com.franksiret.drones.repository.DroneRepository;
import com.franksiret.drones.service.mapper.DroneMapper;
import com.franksiret.drones.web.rest.vm.DroneUpdateVM;
import com.franksiret.drones.web.rest.vm.DroneVM;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DroneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DroneResourceIT {

    private static final String DEFAULT_SERIAL_NUMBER = "0001";
    private static final String UPDATED_SERIAL_NUMBER = "0002";

    private static final Model DEFAULT_MODEL = Model.Cruiserweight;

    private static final Integer DEFAULT_WEIGHT_LIMIT = 500;
    private static final Integer UPDATED_WEIGHT_LIMIT = 100;
    private static final Integer SMALLER_WEIGHT_LIMIT = 400;

    private static final Integer DEFAULT_BATTERY_CAPACITY = 100;
    private static final Integer UPDATED_BATTERY_CAPACITY = 50;
    private static final Integer SMALLER_BATTERY_CAPACITY = 75;

    private static final State DEFAULT_STATE = State.IDLE;
    private static final State UPDATED_STATE = State.LOADING;

    private static final String ENTITY_API_URL = "/api/drones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneMapper droneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDroneMockMvc;

    private Drone drone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Drone createEntity(EntityManager em) {
        Drone drone = new Drone()
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .model(DEFAULT_MODEL)
            .weightLimit(DEFAULT_WEIGHT_LIMIT)
            .batteryCapacity(DEFAULT_BATTERY_CAPACITY)
            .state(DEFAULT_STATE);
        return drone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DroneUpdateVM createUpdatedEntity(EntityManager em) {
        DroneUpdateVM droneUpdateVM = new DroneUpdateVM();
        droneUpdateVM.setBatteryCapacity(UPDATED_BATTERY_CAPACITY);
        droneUpdateVM.setState(UPDATED_STATE);
        return droneUpdateVM;
    }

    @BeforeEach
    public void initTest() {
        drone = createEntity(em);
    }

    private DroneVM droneToVM(Drone drone) {
        DroneVM droneVM = new DroneVM();
        droneVM.setBatteryCapacity(drone.getBatteryCapacity());
        droneVM.setModel(drone.getModel());
        droneVM.setSerialNumber(drone.getSerialNumber());
        droneVM.setWeightLimit(drone.getWeightLimit());
        return droneVM;
    }

    private DroneUpdateVM droneToUpdateVM(Drone drone) {
        DroneUpdateVM droneUpdateVM = new DroneUpdateVM();
        droneUpdateVM.setId(drone.getId());
        droneUpdateVM.setBatteryCapacity(drone.getBatteryCapacity());
        droneUpdateVM.setState(drone.getState());
        return droneUpdateVM;
    }

    @Test
    @Transactional
    void createDrone() throws Exception {
        int databaseSizeBeforeCreate = droneRepository.findAll().size();
        // Create the Drone
        DroneVM droneVM = droneToVM(drone);

        restDroneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(droneVM)))
            .andExpect(status().isCreated());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeCreate + 1);
        Drone testDrone = droneList.get(droneList.size() - 1);
        assertThat(testDrone.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testDrone.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testDrone.getWeightLimit()).isEqualTo(DEFAULT_WEIGHT_LIMIT);
        assertThat(testDrone.getBatteryCapacity()).isEqualTo(DEFAULT_BATTERY_CAPACITY);
        assertThat(testDrone.getState()).isEqualTo(State.IDLE);
    }

    @Test
    @Transactional
    void checkSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = droneRepository.findAll().size();
        // set the field null
        drone.setSerialNumber(null);

        // Create the Drone, which fails.
        DroneVM droneVM = droneToVM(drone);

        restDroneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(droneVM)))
            .andExpect(status().isBadRequest());

        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelIsRequired() throws Exception {
        int databaseSizeBeforeTest = droneRepository.findAll().size();
        // set the field null
        drone.setModel(null);

        // Create the Drone, which fails.
        DroneVM droneVM = droneToVM(drone);

        restDroneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(droneVM)))
            .andExpect(status().isBadRequest());

        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightLimitIsRequired() throws Exception {
        int databaseSizeBeforeTest = droneRepository.findAll().size();
        // set the field null
        drone.setWeightLimit(null);

        // Create the Drone, which fails.
        DroneVM droneVM = droneToVM(drone);

        restDroneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(droneVM)))
            .andExpect(status().isBadRequest());

        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBatteryCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = droneRepository.findAll().size();
        // set the field null
        drone.setBatteryCapacity(null);

        // Create the Drone, which fails.
        DroneVM droneVM = droneToVM(drone);

        restDroneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(droneVM)))
            .andExpect(status().isBadRequest());

        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDrones() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList
        restDroneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drone.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].weightLimit").value(hasItem(DEFAULT_WEIGHT_LIMIT)))
            .andExpect(jsonPath("$.[*].batteryCapacity").value(hasItem(DEFAULT_BATTERY_CAPACITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    void getDrone() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get the drone
        restDroneMockMvc
            .perform(get(ENTITY_API_URL_ID, drone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(drone.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.weightLimit").value(DEFAULT_WEIGHT_LIMIT))
            .andExpect(jsonPath("$.batteryCapacity").value(DEFAULT_BATTERY_CAPACITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    void getAllDronesByBatteryCapacityIsEqualToSomething() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList where batteryCapacity equals to DEFAULT_BATTERY_CAPACITY
        defaultDroneShouldBeFound("batteryCapacity.equals=" + DEFAULT_BATTERY_CAPACITY);

        // Get all the droneList where batteryCapacity equals to UPDATED_BATTERY_CAPACITY
        defaultDroneShouldNotBeFound("batteryCapacity.equals=" + UPDATED_BATTERY_CAPACITY);
    }

    @Test
    @Transactional
    void getAllDronesByBatteryCapacityIsInShouldWork() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList where batteryCapacity in DEFAULT_BATTERY_CAPACITY or UPDATED_BATTERY_CAPACITY
        defaultDroneShouldBeFound("batteryCapacity.in=" + DEFAULT_BATTERY_CAPACITY + "," + UPDATED_BATTERY_CAPACITY);

        // Get all the droneList where batteryCapacity equals to UPDATED_BATTERY_CAPACITY
        defaultDroneShouldNotBeFound("batteryCapacity.in=" + UPDATED_BATTERY_CAPACITY);
    }

    @Test
    @Transactional
    void getAllDronesByBatteryCapacityIsNullOrNotNull() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList where batteryCapacity is not null
        defaultDroneShouldBeFound("batteryCapacity.specified=true");

        // Get all the droneList where batteryCapacity is null
        defaultDroneShouldNotBeFound("batteryCapacity.specified=false");
    }

    @Test
    @Transactional
    void getAllDronesByBatteryCapacityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList where batteryCapacity is greater than or equal to DEFAULT_BATTERY_CAPACITY
        defaultDroneShouldBeFound("batteryCapacity.greaterThanOrEqual=" + DEFAULT_BATTERY_CAPACITY);

        // Get all the droneList where batteryCapacity is greater than or equal to (DEFAULT_BATTERY_CAPACITY + 1)
        defaultDroneShouldNotBeFound("batteryCapacity.greaterThanOrEqual=" + (DEFAULT_BATTERY_CAPACITY + 1));
    }

    @Test
    @Transactional
    void getAllDronesByBatteryCapacityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList where batteryCapacity is less than or equal to DEFAULT_BATTERY_CAPACITY
        defaultDroneShouldBeFound("batteryCapacity.lessThanOrEqual=" + DEFAULT_BATTERY_CAPACITY);

        // Get all the droneList where batteryCapacity is less than or equal to SMALLER_BATTERY_CAPACITY
        defaultDroneShouldNotBeFound("batteryCapacity.lessThanOrEqual=" + SMALLER_BATTERY_CAPACITY);
    }

    @Test
    @Transactional
    void getAllDronesByBatteryCapacityIsLessThanSomething() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList where batteryCapacity is less than DEFAULT_BATTERY_CAPACITY
        defaultDroneShouldNotBeFound("batteryCapacity.lessThan=" + DEFAULT_BATTERY_CAPACITY);

        // Get all the droneList where batteryCapacity is less than (DEFAULT_BATTERY_CAPACITY + 1)
        defaultDroneShouldBeFound("batteryCapacity.lessThan=" + (DEFAULT_BATTERY_CAPACITY + 1));
    }

    @Test
    @Transactional
    void getAllDronesByBatteryCapacityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList where batteryCapacity is greater than DEFAULT_BATTERY_CAPACITY
        defaultDroneShouldNotBeFound("batteryCapacity.greaterThan=" + DEFAULT_BATTERY_CAPACITY);

        // Get all the droneList where batteryCapacity is greater than SMALLER_BATTERY_CAPACITY
        defaultDroneShouldBeFound("batteryCapacity.greaterThan=" + SMALLER_BATTERY_CAPACITY);
    }

    @Test
    @Transactional
    void getAllDronesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList where state equals to DEFAULT_STATE
        defaultDroneShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the droneList where state equals to UPDATED_STATE
        defaultDroneShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllDronesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList where state in DEFAULT_STATE or UPDATED_STATE
        defaultDroneShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the droneList where state equals to UPDATED_STATE
        defaultDroneShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllDronesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        // Get all the droneList where state is not null
        defaultDroneShouldBeFound("state.specified=true");

        // Get all the droneList where state is null
        defaultDroneShouldNotBeFound("state.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDroneShouldBeFound(String filter) throws Exception {
        restDroneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drone.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].weightLimit").value(hasItem(DEFAULT_WEIGHT_LIMIT)))
            .andExpect(jsonPath("$.[*].batteryCapacity").value(hasItem(DEFAULT_BATTERY_CAPACITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));

        // Check, that the count call also returns 1
        restDroneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDroneShouldNotBeFound(String filter) throws Exception {
        restDroneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDroneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDrone() throws Exception {
        // Get the drone
        restDroneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void partialUpdateExistingDrone() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        int databaseSizeBeforeUpdate = droneRepository.findAll().size();

        // Update the drone
        Drone updatedDrone = droneRepository.findById(drone.getId()).get();
        // Disconnect from session so that the updates on updatedDrone are not directly saved in db
        em.detach(updatedDrone);
        updatedDrone.batteryCapacity(UPDATED_BATTERY_CAPACITY).state(UPDATED_STATE);
        DroneUpdateVM droneUpdateVM = droneToUpdateVM(updatedDrone);

        restDroneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, droneUpdateVM.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(droneUpdateVM))
            )
            .andExpect(status().isOk());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
        Drone testDrone = droneList.get(droneList.size() - 1);
        assertThat(testDrone.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testDrone.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testDrone.getWeightLimit()).isEqualTo(DEFAULT_WEIGHT_LIMIT);
        assertThat(testDrone.getBatteryCapacity()).isEqualTo(UPDATED_BATTERY_CAPACITY);
        assertThat(testDrone.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void partialUpdateNonExistingDrone() throws Exception {
        int databaseSizeBeforeUpdate = droneRepository.findAll().size();
        drone.setId(count.incrementAndGet());

        // Create the Drone
        DroneUpdateVM droneUpdateVM = droneToUpdateVM(drone);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDroneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, droneUpdateVM.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(droneUpdateVM))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWithIdMismatchDrone() throws Exception {
        int databaseSizeBeforeUpdate = droneRepository.findAll().size();
        drone.setId(count.incrementAndGet());

        // Create the Drone
        DroneUpdateVM droneUpdateVM = droneToUpdateVM(drone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(droneUpdateVM))
            )
            .andExpect(status().isBadRequest());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWithMissingIdPathParamDrone() throws Exception {
        int databaseSizeBeforeUpdate = droneRepository.findAll().size();
        drone.setId(count.incrementAndGet());

        // Create the Drone
        DroneUpdateVM droneUpdateVM = droneToUpdateVM(drone);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDroneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(droneUpdateVM))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Drone in the database
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDrone() throws Exception {
        // Initialize the database
        droneRepository.saveAndFlush(drone);

        int databaseSizeBeforeDelete = droneRepository.findAll().size();

        // Delete the drone
        restDroneMockMvc
            .perform(delete(ENTITY_API_URL_ID, drone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Drone> droneList = droneRepository.findAll();
        assertThat(droneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
