package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Diagnose;
import com.mycompany.myapp.repository.DiagnoseRepository;
import com.mycompany.myapp.service.dto.DiagnoseDTO;
import com.mycompany.myapp.service.mapper.DiagnoseMapper;
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
 * Integration tests for the {@link DiagnoseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DiagnoseResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/diagnoses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DiagnoseRepository diagnoseRepository;

    @Autowired
    private DiagnoseMapper diagnoseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDiagnoseMockMvc;

    private Diagnose diagnose;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diagnose createEntity(EntityManager em) {
        Diagnose diagnose = new Diagnose().description(DEFAULT_DESCRIPTION);
        return diagnose;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diagnose createUpdatedEntity(EntityManager em) {
        Diagnose diagnose = new Diagnose().description(UPDATED_DESCRIPTION);
        return diagnose;
    }

    @BeforeEach
    public void initTest() {
        diagnose = createEntity(em);
    }

    @Test
    @Transactional
    void createDiagnose() throws Exception {
        int databaseSizeBeforeCreate = diagnoseRepository.findAll().size();
        // Create the Diagnose
        DiagnoseDTO diagnoseDTO = diagnoseMapper.toDto(diagnose);
        restDiagnoseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diagnoseDTO)))
            .andExpect(status().isCreated());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeCreate + 1);
        Diagnose testDiagnose = diagnoseList.get(diagnoseList.size() - 1);
        assertThat(testDiagnose.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createDiagnoseWithExistingId() throws Exception {
        // Create the Diagnose with an existing ID
        diagnose.setId(1L);
        DiagnoseDTO diagnoseDTO = diagnoseMapper.toDto(diagnose);

        int databaseSizeBeforeCreate = diagnoseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDiagnoseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diagnoseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDiagnoses() throws Exception {
        // Initialize the database
        diagnoseRepository.saveAndFlush(diagnose);

        // Get all the diagnoseList
        restDiagnoseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(diagnose.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDiagnose() throws Exception {
        // Initialize the database
        diagnoseRepository.saveAndFlush(diagnose);

        // Get the diagnose
        restDiagnoseMockMvc
            .perform(get(ENTITY_API_URL_ID, diagnose.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(diagnose.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDiagnose() throws Exception {
        // Get the diagnose
        restDiagnoseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDiagnose() throws Exception {
        // Initialize the database
        diagnoseRepository.saveAndFlush(diagnose);

        int databaseSizeBeforeUpdate = diagnoseRepository.findAll().size();

        // Update the diagnose
        Diagnose updatedDiagnose = diagnoseRepository.findById(diagnose.getId()).get();
        // Disconnect from session so that the updates on updatedDiagnose are not directly saved in db
        em.detach(updatedDiagnose);
        updatedDiagnose.description(UPDATED_DESCRIPTION);
        DiagnoseDTO diagnoseDTO = diagnoseMapper.toDto(updatedDiagnose);

        restDiagnoseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diagnoseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(diagnoseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeUpdate);
        Diagnose testDiagnose = diagnoseList.get(diagnoseList.size() - 1);
        assertThat(testDiagnose.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingDiagnose() throws Exception {
        int databaseSizeBeforeUpdate = diagnoseRepository.findAll().size();
        diagnose.setId(count.incrementAndGet());

        // Create the Diagnose
        DiagnoseDTO diagnoseDTO = diagnoseMapper.toDto(diagnose);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiagnoseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, diagnoseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(diagnoseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDiagnose() throws Exception {
        int databaseSizeBeforeUpdate = diagnoseRepository.findAll().size();
        diagnose.setId(count.incrementAndGet());

        // Create the Diagnose
        DiagnoseDTO diagnoseDTO = diagnoseMapper.toDto(diagnose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnoseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(diagnoseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDiagnose() throws Exception {
        int databaseSizeBeforeUpdate = diagnoseRepository.findAll().size();
        diagnose.setId(count.incrementAndGet());

        // Create the Diagnose
        DiagnoseDTO diagnoseDTO = diagnoseMapper.toDto(diagnose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnoseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(diagnoseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDiagnoseWithPatch() throws Exception {
        // Initialize the database
        diagnoseRepository.saveAndFlush(diagnose);

        int databaseSizeBeforeUpdate = diagnoseRepository.findAll().size();

        // Update the diagnose using partial update
        Diagnose partialUpdatedDiagnose = new Diagnose();
        partialUpdatedDiagnose.setId(diagnose.getId());

        restDiagnoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiagnose.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiagnose))
            )
            .andExpect(status().isOk());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeUpdate);
        Diagnose testDiagnose = diagnoseList.get(diagnoseList.size() - 1);
        assertThat(testDiagnose.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDiagnoseWithPatch() throws Exception {
        // Initialize the database
        diagnoseRepository.saveAndFlush(diagnose);

        int databaseSizeBeforeUpdate = diagnoseRepository.findAll().size();

        // Update the diagnose using partial update
        Diagnose partialUpdatedDiagnose = new Diagnose();
        partialUpdatedDiagnose.setId(diagnose.getId());

        partialUpdatedDiagnose.description(UPDATED_DESCRIPTION);

        restDiagnoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDiagnose.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDiagnose))
            )
            .andExpect(status().isOk());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeUpdate);
        Diagnose testDiagnose = diagnoseList.get(diagnoseList.size() - 1);
        assertThat(testDiagnose.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDiagnose() throws Exception {
        int databaseSizeBeforeUpdate = diagnoseRepository.findAll().size();
        diagnose.setId(count.incrementAndGet());

        // Create the Diagnose
        DiagnoseDTO diagnoseDTO = diagnoseMapper.toDto(diagnose);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDiagnoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, diagnoseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(diagnoseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDiagnose() throws Exception {
        int databaseSizeBeforeUpdate = diagnoseRepository.findAll().size();
        diagnose.setId(count.incrementAndGet());

        // Create the Diagnose
        DiagnoseDTO diagnoseDTO = diagnoseMapper.toDto(diagnose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnoseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(diagnoseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDiagnose() throws Exception {
        int databaseSizeBeforeUpdate = diagnoseRepository.findAll().size();
        diagnose.setId(count.incrementAndGet());

        // Create the Diagnose
        DiagnoseDTO diagnoseDTO = diagnoseMapper.toDto(diagnose);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDiagnoseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(diagnoseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Diagnose in the database
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDiagnose() throws Exception {
        // Initialize the database
        diagnoseRepository.saveAndFlush(diagnose);

        int databaseSizeBeforeDelete = diagnoseRepository.findAll().size();

        // Delete the diagnose
        restDiagnoseMockMvc
            .perform(delete(ENTITY_API_URL_ID, diagnose.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Diagnose> diagnoseList = diagnoseRepository.findAll();
        assertThat(diagnoseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
