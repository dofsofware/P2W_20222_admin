package com.techxel.play2win_admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.InfosAbonne;
import com.techxel.play2win_admin.repository.InfosAbonneRepository;
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
 * Integration tests for the {@link InfosAbonneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InfosAbonneResourceIT {

    private static final String ENTITY_API_URL = "/api/infos-abonnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InfosAbonneRepository infosAbonneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInfosAbonneMockMvc;

    private InfosAbonne infosAbonne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfosAbonne createEntity(EntityManager em) {
        InfosAbonne infosAbonne = new InfosAbonne();
        return infosAbonne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfosAbonne createUpdatedEntity(EntityManager em) {
        InfosAbonne infosAbonne = new InfosAbonne();
        return infosAbonne;
    }

    @BeforeEach
    public void initTest() {
        infosAbonne = createEntity(em);
    }

    @Test
    @Transactional
    void createInfosAbonne() throws Exception {
        int databaseSizeBeforeCreate = infosAbonneRepository.findAll().size();
        // Create the InfosAbonne
        restInfosAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infosAbonne)))
            .andExpect(status().isCreated());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeCreate + 1);
        InfosAbonne testInfosAbonne = infosAbonneList.get(infosAbonneList.size() - 1);
    }

    @Test
    @Transactional
    void createInfosAbonneWithExistingId() throws Exception {
        // Create the InfosAbonne with an existing ID
        infosAbonne.setId(1L);

        int databaseSizeBeforeCreate = infosAbonneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfosAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infosAbonne)))
            .andExpect(status().isBadRequest());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInfosAbonnes() throws Exception {
        // Initialize the database
        infosAbonneRepository.saveAndFlush(infosAbonne);

        // Get all the infosAbonneList
        restInfosAbonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infosAbonne.getId().intValue())));
    }

    @Test
    @Transactional
    void getInfosAbonne() throws Exception {
        // Initialize the database
        infosAbonneRepository.saveAndFlush(infosAbonne);

        // Get the infosAbonne
        restInfosAbonneMockMvc
            .perform(get(ENTITY_API_URL_ID, infosAbonne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infosAbonne.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingInfosAbonne() throws Exception {
        // Get the infosAbonne
        restInfosAbonneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInfosAbonne() throws Exception {
        // Initialize the database
        infosAbonneRepository.saveAndFlush(infosAbonne);

        int databaseSizeBeforeUpdate = infosAbonneRepository.findAll().size();

        // Update the infosAbonne
        InfosAbonne updatedInfosAbonne = infosAbonneRepository.findById(infosAbonne.getId()).get();
        // Disconnect from session so that the updates on updatedInfosAbonne are not directly saved in db
        em.detach(updatedInfosAbonne);

        restInfosAbonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInfosAbonne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInfosAbonne))
            )
            .andExpect(status().isOk());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeUpdate);
        InfosAbonne testInfosAbonne = infosAbonneList.get(infosAbonneList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingInfosAbonne() throws Exception {
        int databaseSizeBeforeUpdate = infosAbonneRepository.findAll().size();
        infosAbonne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfosAbonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, infosAbonne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infosAbonne))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInfosAbonne() throws Exception {
        int databaseSizeBeforeUpdate = infosAbonneRepository.findAll().size();
        infosAbonne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfosAbonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(infosAbonne))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInfosAbonne() throws Exception {
        int databaseSizeBeforeUpdate = infosAbonneRepository.findAll().size();
        infosAbonne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfosAbonneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(infosAbonne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInfosAbonneWithPatch() throws Exception {
        // Initialize the database
        infosAbonneRepository.saveAndFlush(infosAbonne);

        int databaseSizeBeforeUpdate = infosAbonneRepository.findAll().size();

        // Update the infosAbonne using partial update
        InfosAbonne partialUpdatedInfosAbonne = new InfosAbonne();
        partialUpdatedInfosAbonne.setId(infosAbonne.getId());

        restInfosAbonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfosAbonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfosAbonne))
            )
            .andExpect(status().isOk());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeUpdate);
        InfosAbonne testInfosAbonne = infosAbonneList.get(infosAbonneList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateInfosAbonneWithPatch() throws Exception {
        // Initialize the database
        infosAbonneRepository.saveAndFlush(infosAbonne);

        int databaseSizeBeforeUpdate = infosAbonneRepository.findAll().size();

        // Update the infosAbonne using partial update
        InfosAbonne partialUpdatedInfosAbonne = new InfosAbonne();
        partialUpdatedInfosAbonne.setId(infosAbonne.getId());

        restInfosAbonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInfosAbonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInfosAbonne))
            )
            .andExpect(status().isOk());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeUpdate);
        InfosAbonne testInfosAbonne = infosAbonneList.get(infosAbonneList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingInfosAbonne() throws Exception {
        int databaseSizeBeforeUpdate = infosAbonneRepository.findAll().size();
        infosAbonne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfosAbonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, infosAbonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infosAbonne))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInfosAbonne() throws Exception {
        int databaseSizeBeforeUpdate = infosAbonneRepository.findAll().size();
        infosAbonne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfosAbonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(infosAbonne))
            )
            .andExpect(status().isBadRequest());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInfosAbonne() throws Exception {
        int databaseSizeBeforeUpdate = infosAbonneRepository.findAll().size();
        infosAbonne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInfosAbonneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(infosAbonne))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InfosAbonne in the database
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInfosAbonne() throws Exception {
        // Initialize the database
        infosAbonneRepository.saveAndFlush(infosAbonne);

        int databaseSizeBeforeDelete = infosAbonneRepository.findAll().size();

        // Delete the infosAbonne
        restInfosAbonneMockMvc
            .perform(delete(ENTITY_API_URL_ID, infosAbonne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InfosAbonne> infosAbonneList = infosAbonneRepository.findAll();
        assertThat(infosAbonneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
