package com.techxel.play2win_admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.Principes;
import com.techxel.play2win_admin.repository.PrincipesRepository;
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
 * Integration tests for the {@link PrincipesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrincipesResourceIT {

    private static final String ENTITY_API_URL = "/api/principes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrincipesRepository principesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrincipesMockMvc;

    private Principes principes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Principes createEntity(EntityManager em) {
        Principes principes = new Principes();
        return principes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Principes createUpdatedEntity(EntityManager em) {
        Principes principes = new Principes();
        return principes;
    }

    @BeforeEach
    public void initTest() {
        principes = createEntity(em);
    }

    @Test
    @Transactional
    void createPrincipes() throws Exception {
        int databaseSizeBeforeCreate = principesRepository.findAll().size();
        // Create the Principes
        restPrincipesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(principes)))
            .andExpect(status().isCreated());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeCreate + 1);
        Principes testPrincipes = principesList.get(principesList.size() - 1);
    }

    @Test
    @Transactional
    void createPrincipesWithExistingId() throws Exception {
        // Create the Principes with an existing ID
        principes.setId(1L);

        int databaseSizeBeforeCreate = principesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrincipesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(principes)))
            .andExpect(status().isBadRequest());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPrincipes() throws Exception {
        // Initialize the database
        principesRepository.saveAndFlush(principes);

        // Get all the principesList
        restPrincipesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(principes.getId().intValue())));
    }

    @Test
    @Transactional
    void getPrincipes() throws Exception {
        // Initialize the database
        principesRepository.saveAndFlush(principes);

        // Get the principes
        restPrincipesMockMvc
            .perform(get(ENTITY_API_URL_ID, principes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(principes.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPrincipes() throws Exception {
        // Get the principes
        restPrincipesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrincipes() throws Exception {
        // Initialize the database
        principesRepository.saveAndFlush(principes);

        int databaseSizeBeforeUpdate = principesRepository.findAll().size();

        // Update the principes
        Principes updatedPrincipes = principesRepository.findById(principes.getId()).get();
        // Disconnect from session so that the updates on updatedPrincipes are not directly saved in db
        em.detach(updatedPrincipes);

        restPrincipesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPrincipes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPrincipes))
            )
            .andExpect(status().isOk());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeUpdate);
        Principes testPrincipes = principesList.get(principesList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPrincipes() throws Exception {
        int databaseSizeBeforeUpdate = principesRepository.findAll().size();
        principes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrincipesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, principes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(principes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrincipes() throws Exception {
        int databaseSizeBeforeUpdate = principesRepository.findAll().size();
        principes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrincipesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(principes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrincipes() throws Exception {
        int databaseSizeBeforeUpdate = principesRepository.findAll().size();
        principes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrincipesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(principes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrincipesWithPatch() throws Exception {
        // Initialize the database
        principesRepository.saveAndFlush(principes);

        int databaseSizeBeforeUpdate = principesRepository.findAll().size();

        // Update the principes using partial update
        Principes partialUpdatedPrincipes = new Principes();
        partialUpdatedPrincipes.setId(principes.getId());

        restPrincipesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrincipes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrincipes))
            )
            .andExpect(status().isOk());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeUpdate);
        Principes testPrincipes = principesList.get(principesList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePrincipesWithPatch() throws Exception {
        // Initialize the database
        principesRepository.saveAndFlush(principes);

        int databaseSizeBeforeUpdate = principesRepository.findAll().size();

        // Update the principes using partial update
        Principes partialUpdatedPrincipes = new Principes();
        partialUpdatedPrincipes.setId(principes.getId());

        restPrincipesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrincipes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrincipes))
            )
            .andExpect(status().isOk());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeUpdate);
        Principes testPrincipes = principesList.get(principesList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPrincipes() throws Exception {
        int databaseSizeBeforeUpdate = principesRepository.findAll().size();
        principes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrincipesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, principes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(principes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrincipes() throws Exception {
        int databaseSizeBeforeUpdate = principesRepository.findAll().size();
        principes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrincipesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(principes))
            )
            .andExpect(status().isBadRequest());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrincipes() throws Exception {
        int databaseSizeBeforeUpdate = principesRepository.findAll().size();
        principes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrincipesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(principes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Principes in the database
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrincipes() throws Exception {
        // Initialize the database
        principesRepository.saveAndFlush(principes);

        int databaseSizeBeforeDelete = principesRepository.findAll().size();

        // Delete the principes
        restPrincipesMockMvc
            .perform(delete(ENTITY_API_URL_ID, principes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Principes> principesList = principesRepository.findAll();
        assertThat(principesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
