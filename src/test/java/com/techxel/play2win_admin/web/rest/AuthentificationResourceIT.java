package com.techxel.play2win_admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.Authentification;
import com.techxel.play2win_admin.repository.AuthentificationRepository;
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
 * Integration tests for the {@link AuthentificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AuthentificationResourceIT {

    private static final String ENTITY_API_URL = "/api/authentifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AuthentificationRepository authentificationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuthentificationMockMvc;

    private Authentification authentification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authentification createEntity(EntityManager em) {
        Authentification authentification = new Authentification();
        return authentification;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Authentification createUpdatedEntity(EntityManager em) {
        Authentification authentification = new Authentification();
        return authentification;
    }

    @BeforeEach
    public void initTest() {
        authentification = createEntity(em);
    }

    @Test
    @Transactional
    void createAuthentification() throws Exception {
        int databaseSizeBeforeCreate = authentificationRepository.findAll().size();
        // Create the Authentification
        restAuthentificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(authentification))
            )
            .andExpect(status().isCreated());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeCreate + 1);
        Authentification testAuthentification = authentificationList.get(authentificationList.size() - 1);
    }

    @Test
    @Transactional
    void createAuthentificationWithExistingId() throws Exception {
        // Create the Authentification with an existing ID
        authentification.setId(1L);

        int databaseSizeBeforeCreate = authentificationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthentificationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(authentification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAuthentifications() throws Exception {
        // Initialize the database
        authentificationRepository.saveAndFlush(authentification);

        // Get all the authentificationList
        restAuthentificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authentification.getId().intValue())));
    }

    @Test
    @Transactional
    void getAuthentification() throws Exception {
        // Initialize the database
        authentificationRepository.saveAndFlush(authentification);

        // Get the authentification
        restAuthentificationMockMvc
            .perform(get(ENTITY_API_URL_ID, authentification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(authentification.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingAuthentification() throws Exception {
        // Get the authentification
        restAuthentificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAuthentification() throws Exception {
        // Initialize the database
        authentificationRepository.saveAndFlush(authentification);

        int databaseSizeBeforeUpdate = authentificationRepository.findAll().size();

        // Update the authentification
        Authentification updatedAuthentification = authentificationRepository.findById(authentification.getId()).get();
        // Disconnect from session so that the updates on updatedAuthentification are not directly saved in db
        em.detach(updatedAuthentification);

        restAuthentificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAuthentification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAuthentification))
            )
            .andExpect(status().isOk());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeUpdate);
        Authentification testAuthentification = authentificationList.get(authentificationList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingAuthentification() throws Exception {
        int databaseSizeBeforeUpdate = authentificationRepository.findAll().size();
        authentification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, authentification.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authentification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuthentification() throws Exception {
        int databaseSizeBeforeUpdate = authentificationRepository.findAll().size();
        authentification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(authentification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuthentification() throws Exception {
        int databaseSizeBeforeUpdate = authentificationRepository.findAll().size();
        authentification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(authentification))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuthentificationWithPatch() throws Exception {
        // Initialize the database
        authentificationRepository.saveAndFlush(authentification);

        int databaseSizeBeforeUpdate = authentificationRepository.findAll().size();

        // Update the authentification using partial update
        Authentification partialUpdatedAuthentification = new Authentification();
        partialUpdatedAuthentification.setId(authentification.getId());

        restAuthentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthentification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuthentification))
            )
            .andExpect(status().isOk());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeUpdate);
        Authentification testAuthentification = authentificationList.get(authentificationList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateAuthentificationWithPatch() throws Exception {
        // Initialize the database
        authentificationRepository.saveAndFlush(authentification);

        int databaseSizeBeforeUpdate = authentificationRepository.findAll().size();

        // Update the authentification using partial update
        Authentification partialUpdatedAuthentification = new Authentification();
        partialUpdatedAuthentification.setId(authentification.getId());

        restAuthentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuthentification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuthentification))
            )
            .andExpect(status().isOk());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeUpdate);
        Authentification testAuthentification = authentificationList.get(authentificationList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingAuthentification() throws Exception {
        int databaseSizeBeforeUpdate = authentificationRepository.findAll().size();
        authentification.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, authentification.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authentification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuthentification() throws Exception {
        int databaseSizeBeforeUpdate = authentificationRepository.findAll().size();
        authentification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authentification))
            )
            .andExpect(status().isBadRequest());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuthentification() throws Exception {
        int databaseSizeBeforeUpdate = authentificationRepository.findAll().size();
        authentification.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuthentificationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(authentification))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Authentification in the database
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuthentification() throws Exception {
        // Initialize the database
        authentificationRepository.saveAndFlush(authentification);

        int databaseSizeBeforeDelete = authentificationRepository.findAll().size();

        // Delete the authentification
        restAuthentificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, authentification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Authentification> authentificationList = authentificationRepository.findAll();
        assertThat(authentificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
