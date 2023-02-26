package com.franksiret.drones.web.rest;

import com.franksiret.drones.IntegrationTest;
import com.franksiret.drones.domain.Medication;
import com.franksiret.drones.web.rest.vm.MedicationFormVM;
import javax.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

/**
 * Integration tests for the {@link MedicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicationResourceUtil {

    public static final String DEFAULT_NAME = "AAAAAAAAAA";
    public static final String UPDATED_NAME = "BBBBBBBBBB";

    public static final Integer DEFAULT_WEIGHT = 1;
    public static final Integer UPDATED_WEIGHT = 2;
    public static final Integer SMALLER_WEIGHT = 1 - 1;

    public static final String DEFAULT_CODE = "AAAAAAAAAA";
    public static final String UPDATED_CODE = "BBBBBBBBBB";

    public static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    public static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    public static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    public static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medication createEntity(EntityManager em) {
        Medication medication = new Medication()
            .name(DEFAULT_NAME)
            .weight(DEFAULT_WEIGHT)
            .code(DEFAULT_CODE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return medication;
    }

    public static MedicationFormVM medicationToFormVM(Medication medication) {
        MedicationFormVM medicationFormVM = new MedicationFormVM();
        medicationFormVM.setCode(medication.getCode());
        medicationFormVM.setId(medication.getId());
        medicationFormVM.setName(medication.getName());
        medicationFormVM.setWeight(medication.getWeight());
        return medicationFormVM;
    }
}
