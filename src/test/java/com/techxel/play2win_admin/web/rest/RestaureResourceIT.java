package com.techxel.play2win_admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.Restaure;
import com.techxel.play2win_admin.repository.RestaureRepository;
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
 * Integration tests for the {@link RestaureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestaureResourceIT {

    private static final String ENTITY_API_URL = "/api/restaures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaureRepository restaureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaureMockMvc;

    private Restaure restaure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaure createEntity(EntityManager em) {
        Restaure restaure = new Restaure();
        return restaure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaure createUpdatedEntity(EntityManager em) {
        Restaure restaure = new Restaure();
        return restaure;
    }

    @BeforeEach
    public void initTest() {
        restaure = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaure() throws Exception {
        int databaseSizeBeforeCreate = restaureRepository.findAll().size();
        // Create the Restaure
        restRestaureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaure)))
            .andExpect(status().isCreated());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeCreate + 1);
        Restaure testRestaure = restaureList.get(restaureList.size() - 1);
    }

    @Test
    @Transactional
    void createRestaureWithExistingId() throws Exception {
        // Create the Restaure with an existing ID
        restaure.setId(1L);

        int databaseSizeBeforeCreate = restaureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaure)))
            .andExpect(status().isBadRequest());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRestaures() throws Exception {
        // Initialize the database
        restaureRepository.saveAndFlush(restaure);

        // Get all the restaureList
        restRestaureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaure.getId().intValue())));
    }

    @Test
    @Transactional
    void getRestaure() throws Exception {
        // Initialize the database
        restaureRepository.saveAndFlush(restaure);

        // Get the restaure
        restRestaureMockMvc
            .perform(get(ENTITY_API_URL_ID, restaure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaure.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingRestaure() throws Exception {
        // Get the restaure
        restRestaureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaure() throws Exception {
        // Initialize the database
        restaureRepository.saveAndFlush(restaure);

        int databaseSizeBeforeUpdate = restaureRepository.findAll().size();

        // Update the restaure
        Restaure updatedRestaure = restaureRepository.findById(restaure.getId()).get();
        // Disconnect from session so that the updates on updatedRestaure are not directly saved in db
        em.detach(updatedRestaure);

        restRestaureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRestaure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRestaure))
            )
            .andExpect(status().isOk());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeUpdate);
        Restaure testRestaure = restaureList.get(restaureList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingRestaure() throws Exception {
        int databaseSizeBeforeUpdate = restaureRepository.findAll().size();
        restaure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaure() throws Exception {
        int databaseSizeBeforeUpdate = restaureRepository.findAll().size();
        restaure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaure() throws Exception {
        int databaseSizeBeforeUpdate = restaureRepository.findAll().size();
        restaure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaureWithPatch() throws Exception {
        // Initialize the database
        restaureRepository.saveAndFlush(restaure);

        int databaseSizeBeforeUpdate = restaureRepository.findAll().size();

        // Update the restaure using partial update
        Restaure partialUpdatedRestaure = new Restaure();
        partialUpdatedRestaure.setId(restaure.getId());

        restRestaureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaure))
            )
            .andExpect(status().isOk());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeUpdate);
        Restaure testRestaure = restaureList.get(restaureList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateRestaureWithPatch() throws Exception {
        // Initialize the database
        restaureRepository.saveAndFlush(restaure);

        int databaseSizeBeforeUpdate = restaureRepository.findAll().size();

        // Update the restaure using partial update
        Restaure partialUpdatedRestaure = new Restaure();
        partialUpdatedRestaure.setId(restaure.getId());

        restRestaureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaure))
            )
            .andExpect(status().isOk());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeUpdate);
        Restaure testRestaure = restaureList.get(restaureList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingRestaure() throws Exception {
        int databaseSizeBeforeUpdate = restaureRepository.findAll().size();
        restaure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaure() throws Exception {
        int databaseSizeBeforeUpdate = restaureRepository.findAll().size();
        restaure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaure() throws Exception {
        int databaseSizeBeforeUpdate = restaureRepository.findAll().size();
        restaure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(restaure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaure in the database
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaure() throws Exception {
        // Initialize the database
        restaureRepository.saveAndFlush(restaure);

        int databaseSizeBeforeDelete = restaureRepository.findAll().size();

        // Delete the restaure
        restRestaureMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restaure> restaureList = restaureRepository.findAll();
        assertThat(restaureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
