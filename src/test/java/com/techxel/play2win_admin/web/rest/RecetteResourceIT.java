package com.techxel.play2win_admin.web.rest;

import static com.techxel.play2win_admin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.Recette;
import com.techxel.play2win_admin.domain.enumeration.ChoixDuGain;
import com.techxel.play2win_admin.repository.RecetteRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link RecetteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecetteResourceIT {

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final ChoixDuGain DEFAULT_CHOIX_DU_GAIN = ChoixDuGain.MINUTES;
    private static final ChoixDuGain UPDATED_CHOIX_DU_GAIN = ChoixDuGain.INTERNET;

    private static final String ENTITY_API_URL = "/api/recettes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecetteRepository recetteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecetteMockMvc;

    private Recette recette;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recette createEntity(EntityManager em) {
        Recette recette = new Recette()
            .telephone(DEFAULT_TELEPHONE)
            .createdAt(DEFAULT_CREATED_AT)
            .montant(DEFAULT_MONTANT)
            .choixDuGain(DEFAULT_CHOIX_DU_GAIN);
        return recette;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recette createUpdatedEntity(EntityManager em) {
        Recette recette = new Recette()
            .telephone(UPDATED_TELEPHONE)
            .createdAt(UPDATED_CREATED_AT)
            .montant(UPDATED_MONTANT)
            .choixDuGain(UPDATED_CHOIX_DU_GAIN);
        return recette;
    }

    @BeforeEach
    public void initTest() {
        recette = createEntity(em);
    }

    @Test
    @Transactional
    void createRecette() throws Exception {
        int databaseSizeBeforeCreate = recetteRepository.findAll().size();
        // Create the Recette
        restRecetteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recette)))
            .andExpect(status().isCreated());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeCreate + 1);
        Recette testRecette = recetteList.get(recetteList.size() - 1);
        assertThat(testRecette.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testRecette.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRecette.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testRecette.getChoixDuGain()).isEqualTo(DEFAULT_CHOIX_DU_GAIN);
    }

    @Test
    @Transactional
    void createRecetteWithExistingId() throws Exception {
        // Create the Recette with an existing ID
        recette.setId(1L);

        int databaseSizeBeforeCreate = recetteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecetteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recette)))
            .andExpect(status().isBadRequest());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = recetteRepository.findAll().size();
        // set the field null
        recette.setTelephone(null);

        // Create the Recette, which fails.

        restRecetteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recette)))
            .andExpect(status().isBadRequest());

        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = recetteRepository.findAll().size();
        // set the field null
        recette.setCreatedAt(null);

        // Create the Recette, which fails.

        restRecetteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recette)))
            .andExpect(status().isBadRequest());

        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = recetteRepository.findAll().size();
        // set the field null
        recette.setMontant(null);

        // Create the Recette, which fails.

        restRecetteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recette)))
            .andExpect(status().isBadRequest());

        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChoixDuGainIsRequired() throws Exception {
        int databaseSizeBeforeTest = recetteRepository.findAll().size();
        // set the field null
        recette.setChoixDuGain(null);

        // Create the Recette, which fails.

        restRecetteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recette)))
            .andExpect(status().isBadRequest());

        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecettes() throws Exception {
        // Initialize the database
        recetteRepository.saveAndFlush(recette);

        // Get all the recetteList
        restRecetteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recette.getId().intValue())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].choixDuGain").value(hasItem(DEFAULT_CHOIX_DU_GAIN.toString())));
    }

    @Test
    @Transactional
    void getRecette() throws Exception {
        // Initialize the database
        recetteRepository.saveAndFlush(recette);

        // Get the recette
        restRecetteMockMvc
            .perform(get(ENTITY_API_URL_ID, recette.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recette.getId().intValue()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.choixDuGain").value(DEFAULT_CHOIX_DU_GAIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRecette() throws Exception {
        // Get the recette
        restRecetteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecette() throws Exception {
        // Initialize the database
        recetteRepository.saveAndFlush(recette);

        int databaseSizeBeforeUpdate = recetteRepository.findAll().size();

        // Update the recette
        Recette updatedRecette = recetteRepository.findById(recette.getId()).get();
        // Disconnect from session so that the updates on updatedRecette are not directly saved in db
        em.detach(updatedRecette);
        updatedRecette
            .telephone(UPDATED_TELEPHONE)
            .createdAt(UPDATED_CREATED_AT)
            .montant(UPDATED_MONTANT)
            .choixDuGain(UPDATED_CHOIX_DU_GAIN);

        restRecetteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecette.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRecette))
            )
            .andExpect(status().isOk());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeUpdate);
        Recette testRecette = recetteList.get(recetteList.size() - 1);
        assertThat(testRecette.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testRecette.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRecette.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testRecette.getChoixDuGain()).isEqualTo(UPDATED_CHOIX_DU_GAIN);
    }

    @Test
    @Transactional
    void putNonExistingRecette() throws Exception {
        int databaseSizeBeforeUpdate = recetteRepository.findAll().size();
        recette.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecetteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recette.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recette))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecette() throws Exception {
        int databaseSizeBeforeUpdate = recetteRepository.findAll().size();
        recette.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recette))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecette() throws Exception {
        int databaseSizeBeforeUpdate = recetteRepository.findAll().size();
        recette.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recette)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecetteWithPatch() throws Exception {
        // Initialize the database
        recetteRepository.saveAndFlush(recette);

        int databaseSizeBeforeUpdate = recetteRepository.findAll().size();

        // Update the recette using partial update
        Recette partialUpdatedRecette = new Recette();
        partialUpdatedRecette.setId(recette.getId());

        restRecetteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecette.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecette))
            )
            .andExpect(status().isOk());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeUpdate);
        Recette testRecette = recetteList.get(recetteList.size() - 1);
        assertThat(testRecette.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testRecette.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRecette.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testRecette.getChoixDuGain()).isEqualTo(DEFAULT_CHOIX_DU_GAIN);
    }

    @Test
    @Transactional
    void fullUpdateRecetteWithPatch() throws Exception {
        // Initialize the database
        recetteRepository.saveAndFlush(recette);

        int databaseSizeBeforeUpdate = recetteRepository.findAll().size();

        // Update the recette using partial update
        Recette partialUpdatedRecette = new Recette();
        partialUpdatedRecette.setId(recette.getId());

        partialUpdatedRecette
            .telephone(UPDATED_TELEPHONE)
            .createdAt(UPDATED_CREATED_AT)
            .montant(UPDATED_MONTANT)
            .choixDuGain(UPDATED_CHOIX_DU_GAIN);

        restRecetteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecette.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecette))
            )
            .andExpect(status().isOk());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeUpdate);
        Recette testRecette = recetteList.get(recetteList.size() - 1);
        assertThat(testRecette.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testRecette.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRecette.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testRecette.getChoixDuGain()).isEqualTo(UPDATED_CHOIX_DU_GAIN);
    }

    @Test
    @Transactional
    void patchNonExistingRecette() throws Exception {
        int databaseSizeBeforeUpdate = recetteRepository.findAll().size();
        recette.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecetteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recette.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recette))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecette() throws Exception {
        int databaseSizeBeforeUpdate = recetteRepository.findAll().size();
        recette.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recette))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecette() throws Exception {
        int databaseSizeBeforeUpdate = recetteRepository.findAll().size();
        recette.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecetteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recette)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recette in the database
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecette() throws Exception {
        // Initialize the database
        recetteRepository.saveAndFlush(recette);

        int databaseSizeBeforeDelete = recetteRepository.findAll().size();

        // Delete the recette
        restRecetteMockMvc
            .perform(delete(ENTITY_API_URL_ID, recette.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Recette> recetteList = recetteRepository.findAll();
        assertThat(recetteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
