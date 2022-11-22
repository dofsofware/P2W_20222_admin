package com.techxel.play2win_admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.MotDePasseSetting;
import com.techxel.play2win_admin.repository.MotDePasseSettingRepository;
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
 * Integration tests for the {@link MotDePasseSettingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MotDePasseSettingResourceIT {

    private static final String ENTITY_API_URL = "/api/mot-de-passe-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MotDePasseSettingRepository motDePasseSettingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMotDePasseSettingMockMvc;

    private MotDePasseSetting motDePasseSetting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MotDePasseSetting createEntity(EntityManager em) {
        MotDePasseSetting motDePasseSetting = new MotDePasseSetting();
        return motDePasseSetting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MotDePasseSetting createUpdatedEntity(EntityManager em) {
        MotDePasseSetting motDePasseSetting = new MotDePasseSetting();
        return motDePasseSetting;
    }

    @BeforeEach
    public void initTest() {
        motDePasseSetting = createEntity(em);
    }

    @Test
    @Transactional
    void createMotDePasseSetting() throws Exception {
        int databaseSizeBeforeCreate = motDePasseSettingRepository.findAll().size();
        // Create the MotDePasseSetting
        restMotDePasseSettingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motDePasseSetting))
            )
            .andExpect(status().isCreated());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeCreate + 1);
        MotDePasseSetting testMotDePasseSetting = motDePasseSettingList.get(motDePasseSettingList.size() - 1);
    }

    @Test
    @Transactional
    void createMotDePasseSettingWithExistingId() throws Exception {
        // Create the MotDePasseSetting with an existing ID
        motDePasseSetting.setId(1L);

        int databaseSizeBeforeCreate = motDePasseSettingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotDePasseSettingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motDePasseSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMotDePasseSettings() throws Exception {
        // Initialize the database
        motDePasseSettingRepository.saveAndFlush(motDePasseSetting);

        // Get all the motDePasseSettingList
        restMotDePasseSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motDePasseSetting.getId().intValue())));
    }

    @Test
    @Transactional
    void getMotDePasseSetting() throws Exception {
        // Initialize the database
        motDePasseSettingRepository.saveAndFlush(motDePasseSetting);

        // Get the motDePasseSetting
        restMotDePasseSettingMockMvc
            .perform(get(ENTITY_API_URL_ID, motDePasseSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(motDePasseSetting.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMotDePasseSetting() throws Exception {
        // Get the motDePasseSetting
        restMotDePasseSettingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMotDePasseSetting() throws Exception {
        // Initialize the database
        motDePasseSettingRepository.saveAndFlush(motDePasseSetting);

        int databaseSizeBeforeUpdate = motDePasseSettingRepository.findAll().size();

        // Update the motDePasseSetting
        MotDePasseSetting updatedMotDePasseSetting = motDePasseSettingRepository.findById(motDePasseSetting.getId()).get();
        // Disconnect from session so that the updates on updatedMotDePasseSetting are not directly saved in db
        em.detach(updatedMotDePasseSetting);

        restMotDePasseSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMotDePasseSetting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMotDePasseSetting))
            )
            .andExpect(status().isOk());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeUpdate);
        MotDePasseSetting testMotDePasseSetting = motDePasseSettingList.get(motDePasseSettingList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingMotDePasseSetting() throws Exception {
        int databaseSizeBeforeUpdate = motDePasseSettingRepository.findAll().size();
        motDePasseSetting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotDePasseSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, motDePasseSetting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motDePasseSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMotDePasseSetting() throws Exception {
        int databaseSizeBeforeUpdate = motDePasseSettingRepository.findAll().size();
        motDePasseSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotDePasseSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(motDePasseSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMotDePasseSetting() throws Exception {
        int databaseSizeBeforeUpdate = motDePasseSettingRepository.findAll().size();
        motDePasseSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotDePasseSettingMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(motDePasseSetting))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMotDePasseSettingWithPatch() throws Exception {
        // Initialize the database
        motDePasseSettingRepository.saveAndFlush(motDePasseSetting);

        int databaseSizeBeforeUpdate = motDePasseSettingRepository.findAll().size();

        // Update the motDePasseSetting using partial update
        MotDePasseSetting partialUpdatedMotDePasseSetting = new MotDePasseSetting();
        partialUpdatedMotDePasseSetting.setId(motDePasseSetting.getId());

        restMotDePasseSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotDePasseSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotDePasseSetting))
            )
            .andExpect(status().isOk());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeUpdate);
        MotDePasseSetting testMotDePasseSetting = motDePasseSettingList.get(motDePasseSettingList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateMotDePasseSettingWithPatch() throws Exception {
        // Initialize the database
        motDePasseSettingRepository.saveAndFlush(motDePasseSetting);

        int databaseSizeBeforeUpdate = motDePasseSettingRepository.findAll().size();

        // Update the motDePasseSetting using partial update
        MotDePasseSetting partialUpdatedMotDePasseSetting = new MotDePasseSetting();
        partialUpdatedMotDePasseSetting.setId(motDePasseSetting.getId());

        restMotDePasseSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMotDePasseSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMotDePasseSetting))
            )
            .andExpect(status().isOk());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeUpdate);
        MotDePasseSetting testMotDePasseSetting = motDePasseSettingList.get(motDePasseSettingList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingMotDePasseSetting() throws Exception {
        int databaseSizeBeforeUpdate = motDePasseSettingRepository.findAll().size();
        motDePasseSetting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotDePasseSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, motDePasseSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motDePasseSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMotDePasseSetting() throws Exception {
        int databaseSizeBeforeUpdate = motDePasseSettingRepository.findAll().size();
        motDePasseSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotDePasseSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motDePasseSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMotDePasseSetting() throws Exception {
        int databaseSizeBeforeUpdate = motDePasseSettingRepository.findAll().size();
        motDePasseSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMotDePasseSettingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(motDePasseSetting))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MotDePasseSetting in the database
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMotDePasseSetting() throws Exception {
        // Initialize the database
        motDePasseSettingRepository.saveAndFlush(motDePasseSetting);

        int databaseSizeBeforeDelete = motDePasseSettingRepository.findAll().size();

        // Delete the motDePasseSetting
        restMotDePasseSettingMockMvc
            .perform(delete(ENTITY_API_URL_ID, motDePasseSetting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MotDePasseSetting> motDePasseSettingList = motDePasseSettingRepository.findAll();
        assertThat(motDePasseSettingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
