package com.techxel.play2win_admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.SaisieCode;
import com.techxel.play2win_admin.repository.SaisieCodeRepository;
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
 * Integration tests for the {@link SaisieCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SaisieCodeResourceIT {

    private static final String ENTITY_API_URL = "/api/saisie-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SaisieCodeRepository saisieCodeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaisieCodeMockMvc;

    private SaisieCode saisieCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaisieCode createEntity(EntityManager em) {
        SaisieCode saisieCode = new SaisieCode();
        return saisieCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaisieCode createUpdatedEntity(EntityManager em) {
        SaisieCode saisieCode = new SaisieCode();
        return saisieCode;
    }

    @BeforeEach
    public void initTest() {
        saisieCode = createEntity(em);
    }

    @Test
    @Transactional
    void createSaisieCode() throws Exception {
        int databaseSizeBeforeCreate = saisieCodeRepository.findAll().size();
        // Create the SaisieCode
        restSaisieCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(saisieCode)))
            .andExpect(status().isCreated());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeCreate + 1);
        SaisieCode testSaisieCode = saisieCodeList.get(saisieCodeList.size() - 1);
    }

    @Test
    @Transactional
    void createSaisieCodeWithExistingId() throws Exception {
        // Create the SaisieCode with an existing ID
        saisieCode.setId(1L);

        int databaseSizeBeforeCreate = saisieCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaisieCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(saisieCode)))
            .andExpect(status().isBadRequest());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSaisieCodes() throws Exception {
        // Initialize the database
        saisieCodeRepository.saveAndFlush(saisieCode);

        // Get all the saisieCodeList
        restSaisieCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saisieCode.getId().intValue())));
    }

    @Test
    @Transactional
    void getSaisieCode() throws Exception {
        // Initialize the database
        saisieCodeRepository.saveAndFlush(saisieCode);

        // Get the saisieCode
        restSaisieCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, saisieCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(saisieCode.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSaisieCode() throws Exception {
        // Get the saisieCode
        restSaisieCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSaisieCode() throws Exception {
        // Initialize the database
        saisieCodeRepository.saveAndFlush(saisieCode);

        int databaseSizeBeforeUpdate = saisieCodeRepository.findAll().size();

        // Update the saisieCode
        SaisieCode updatedSaisieCode = saisieCodeRepository.findById(saisieCode.getId()).get();
        // Disconnect from session so that the updates on updatedSaisieCode are not directly saved in db
        em.detach(updatedSaisieCode);

        restSaisieCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSaisieCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSaisieCode))
            )
            .andExpect(status().isOk());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeUpdate);
        SaisieCode testSaisieCode = saisieCodeList.get(saisieCodeList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingSaisieCode() throws Exception {
        int databaseSizeBeforeUpdate = saisieCodeRepository.findAll().size();
        saisieCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaisieCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saisieCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(saisieCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSaisieCode() throws Exception {
        int databaseSizeBeforeUpdate = saisieCodeRepository.findAll().size();
        saisieCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisieCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(saisieCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSaisieCode() throws Exception {
        int databaseSizeBeforeUpdate = saisieCodeRepository.findAll().size();
        saisieCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisieCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(saisieCode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaisieCodeWithPatch() throws Exception {
        // Initialize the database
        saisieCodeRepository.saveAndFlush(saisieCode);

        int databaseSizeBeforeUpdate = saisieCodeRepository.findAll().size();

        // Update the saisieCode using partial update
        SaisieCode partialUpdatedSaisieCode = new SaisieCode();
        partialUpdatedSaisieCode.setId(saisieCode.getId());

        restSaisieCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaisieCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSaisieCode))
            )
            .andExpect(status().isOk());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeUpdate);
        SaisieCode testSaisieCode = saisieCodeList.get(saisieCodeList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateSaisieCodeWithPatch() throws Exception {
        // Initialize the database
        saisieCodeRepository.saveAndFlush(saisieCode);

        int databaseSizeBeforeUpdate = saisieCodeRepository.findAll().size();

        // Update the saisieCode using partial update
        SaisieCode partialUpdatedSaisieCode = new SaisieCode();
        partialUpdatedSaisieCode.setId(saisieCode.getId());

        restSaisieCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaisieCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSaisieCode))
            )
            .andExpect(status().isOk());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeUpdate);
        SaisieCode testSaisieCode = saisieCodeList.get(saisieCodeList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingSaisieCode() throws Exception {
        int databaseSizeBeforeUpdate = saisieCodeRepository.findAll().size();
        saisieCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaisieCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saisieCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(saisieCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSaisieCode() throws Exception {
        int databaseSizeBeforeUpdate = saisieCodeRepository.findAll().size();
        saisieCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisieCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(saisieCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSaisieCode() throws Exception {
        int databaseSizeBeforeUpdate = saisieCodeRepository.findAll().size();
        saisieCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaisieCodeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(saisieCode))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaisieCode in the database
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSaisieCode() throws Exception {
        // Initialize the database
        saisieCodeRepository.saveAndFlush(saisieCode);

        int databaseSizeBeforeDelete = saisieCodeRepository.findAll().size();

        // Delete the saisieCode
        restSaisieCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, saisieCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SaisieCode> saisieCodeList = saisieCodeRepository.findAll();
        assertThat(saisieCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
