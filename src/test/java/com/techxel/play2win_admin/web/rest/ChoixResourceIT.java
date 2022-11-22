package com.techxel.play2win_admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.Choix;
import com.techxel.play2win_admin.repository.ChoixRepository;
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
 * Integration tests for the {@link ChoixResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChoixResourceIT {

    private static final String ENTITY_API_URL = "/api/choixes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ChoixRepository choixRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChoixMockMvc;

    private Choix choix;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Choix createEntity(EntityManager em) {
        Choix choix = new Choix();
        return choix;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Choix createUpdatedEntity(EntityManager em) {
        Choix choix = new Choix();
        return choix;
    }

    @BeforeEach
    public void initTest() {
        choix = createEntity(em);
    }

    @Test
    @Transactional
    void createChoix() throws Exception {
        int databaseSizeBeforeCreate = choixRepository.findAll().size();
        // Create the Choix
        restChoixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(choix)))
            .andExpect(status().isCreated());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeCreate + 1);
        Choix testChoix = choixList.get(choixList.size() - 1);
    }

    @Test
    @Transactional
    void createChoixWithExistingId() throws Exception {
        // Create the Choix with an existing ID
        choix.setId(1L);

        int databaseSizeBeforeCreate = choixRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChoixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(choix)))
            .andExpect(status().isBadRequest());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllChoixes() throws Exception {
        // Initialize the database
        choixRepository.saveAndFlush(choix);

        // Get all the choixList
        restChoixMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(choix.getId().intValue())));
    }

    @Test
    @Transactional
    void getChoix() throws Exception {
        // Initialize the database
        choixRepository.saveAndFlush(choix);

        // Get the choix
        restChoixMockMvc
            .perform(get(ENTITY_API_URL_ID, choix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(choix.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingChoix() throws Exception {
        // Get the choix
        restChoixMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewChoix() throws Exception {
        // Initialize the database
        choixRepository.saveAndFlush(choix);

        int databaseSizeBeforeUpdate = choixRepository.findAll().size();

        // Update the choix
        Choix updatedChoix = choixRepository.findById(choix.getId()).get();
        // Disconnect from session so that the updates on updatedChoix are not directly saved in db
        em.detach(updatedChoix);

        restChoixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChoix.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChoix))
            )
            .andExpect(status().isOk());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeUpdate);
        Choix testChoix = choixList.get(choixList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingChoix() throws Exception {
        int databaseSizeBeforeUpdate = choixRepository.findAll().size();
        choix.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChoixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, choix.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(choix))
            )
            .andExpect(status().isBadRequest());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChoix() throws Exception {
        int databaseSizeBeforeUpdate = choixRepository.findAll().size();
        choix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChoixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(choix))
            )
            .andExpect(status().isBadRequest());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChoix() throws Exception {
        int databaseSizeBeforeUpdate = choixRepository.findAll().size();
        choix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChoixMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(choix)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChoixWithPatch() throws Exception {
        // Initialize the database
        choixRepository.saveAndFlush(choix);

        int databaseSizeBeforeUpdate = choixRepository.findAll().size();

        // Update the choix using partial update
        Choix partialUpdatedChoix = new Choix();
        partialUpdatedChoix.setId(choix.getId());

        restChoixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChoix.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChoix))
            )
            .andExpect(status().isOk());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeUpdate);
        Choix testChoix = choixList.get(choixList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateChoixWithPatch() throws Exception {
        // Initialize the database
        choixRepository.saveAndFlush(choix);

        int databaseSizeBeforeUpdate = choixRepository.findAll().size();

        // Update the choix using partial update
        Choix partialUpdatedChoix = new Choix();
        partialUpdatedChoix.setId(choix.getId());

        restChoixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChoix.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChoix))
            )
            .andExpect(status().isOk());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeUpdate);
        Choix testChoix = choixList.get(choixList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingChoix() throws Exception {
        int databaseSizeBeforeUpdate = choixRepository.findAll().size();
        choix.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChoixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, choix.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(choix))
            )
            .andExpect(status().isBadRequest());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChoix() throws Exception {
        int databaseSizeBeforeUpdate = choixRepository.findAll().size();
        choix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChoixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(choix))
            )
            .andExpect(status().isBadRequest());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChoix() throws Exception {
        int databaseSizeBeforeUpdate = choixRepository.findAll().size();
        choix.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChoixMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(choix)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Choix in the database
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChoix() throws Exception {
        // Initialize the database
        choixRepository.saveAndFlush(choix);

        int databaseSizeBeforeDelete = choixRepository.findAll().size();

        // Delete the choix
        restChoixMockMvc
            .perform(delete(ENTITY_API_URL_ID, choix.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Choix> choixList = choixRepository.findAll();
        assertThat(choixList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
