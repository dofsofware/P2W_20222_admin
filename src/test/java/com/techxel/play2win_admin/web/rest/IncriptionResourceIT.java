package com.techxel.play2win_admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.Incription;
import com.techxel.play2win_admin.repository.IncriptionRepository;
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
 * Integration tests for the {@link IncriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IncriptionResourceIT {

    private static final String ENTITY_API_URL = "/api/incriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IncriptionRepository incriptionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncriptionMockMvc;

    private Incription incription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Incription createEntity(EntityManager em) {
        Incription incription = new Incription();
        return incription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Incription createUpdatedEntity(EntityManager em) {
        Incription incription = new Incription();
        return incription;
    }

    @BeforeEach
    public void initTest() {
        incription = createEntity(em);
    }

    @Test
    @Transactional
    void createIncription() throws Exception {
        int databaseSizeBeforeCreate = incriptionRepository.findAll().size();
        // Create the Incription
        restIncriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incription)))
            .andExpect(status().isCreated());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Incription testIncription = incriptionList.get(incriptionList.size() - 1);
    }

    @Test
    @Transactional
    void createIncriptionWithExistingId() throws Exception {
        // Create the Incription with an existing ID
        incription.setId(1L);

        int databaseSizeBeforeCreate = incriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incription)))
            .andExpect(status().isBadRequest());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIncriptions() throws Exception {
        // Initialize the database
        incriptionRepository.saveAndFlush(incription);

        // Get all the incriptionList
        restIncriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incription.getId().intValue())));
    }

    @Test
    @Transactional
    void getIncription() throws Exception {
        // Initialize the database
        incriptionRepository.saveAndFlush(incription);

        // Get the incription
        restIncriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, incription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(incription.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingIncription() throws Exception {
        // Get the incription
        restIncriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIncription() throws Exception {
        // Initialize the database
        incriptionRepository.saveAndFlush(incription);

        int databaseSizeBeforeUpdate = incriptionRepository.findAll().size();

        // Update the incription
        Incription updatedIncription = incriptionRepository.findById(incription.getId()).get();
        // Disconnect from session so that the updates on updatedIncription are not directly saved in db
        em.detach(updatedIncription);

        restIncriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIncription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIncription))
            )
            .andExpect(status().isOk());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeUpdate);
        Incription testIncription = incriptionList.get(incriptionList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingIncription() throws Exception {
        int databaseSizeBeforeUpdate = incriptionRepository.findAll().size();
        incription.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, incription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncription() throws Exception {
        int databaseSizeBeforeUpdate = incriptionRepository.findAll().size();
        incription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(incription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncription() throws Exception {
        int databaseSizeBeforeUpdate = incriptionRepository.findAll().size();
        incription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(incription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncriptionWithPatch() throws Exception {
        // Initialize the database
        incriptionRepository.saveAndFlush(incription);

        int databaseSizeBeforeUpdate = incriptionRepository.findAll().size();

        // Update the incription using partial update
        Incription partialUpdatedIncription = new Incription();
        partialUpdatedIncription.setId(incription.getId());

        restIncriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncription))
            )
            .andExpect(status().isOk());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeUpdate);
        Incription testIncription = incriptionList.get(incriptionList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateIncriptionWithPatch() throws Exception {
        // Initialize the database
        incriptionRepository.saveAndFlush(incription);

        int databaseSizeBeforeUpdate = incriptionRepository.findAll().size();

        // Update the incription using partial update
        Incription partialUpdatedIncription = new Incription();
        partialUpdatedIncription.setId(incription.getId());

        restIncriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncription))
            )
            .andExpect(status().isOk());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeUpdate);
        Incription testIncription = incriptionList.get(incriptionList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingIncription() throws Exception {
        int databaseSizeBeforeUpdate = incriptionRepository.findAll().size();
        incription.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, incription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncription() throws Exception {
        int databaseSizeBeforeUpdate = incriptionRepository.findAll().size();
        incription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(incription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncription() throws Exception {
        int databaseSizeBeforeUpdate = incriptionRepository.findAll().size();
        incription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncriptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(incription))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Incription in the database
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncription() throws Exception {
        // Initialize the database
        incriptionRepository.saveAndFlush(incription);

        int databaseSizeBeforeDelete = incriptionRepository.findAll().size();

        // Delete the incription
        restIncriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, incription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Incription> incriptionList = incriptionRepository.findAll();
        assertThat(incriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
