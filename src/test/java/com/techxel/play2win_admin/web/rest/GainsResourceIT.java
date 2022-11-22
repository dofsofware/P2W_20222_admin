package com.techxel.play2win_admin.web.rest;

import static com.techxel.play2win_admin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.Gains;
import com.techxel.play2win_admin.repository.GainsRepository;
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
 * Integration tests for the {@link GainsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GainsResourceIT {

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBBBB";

    private static final Double DEFAULT_MINUTE = 1D;
    private static final Double UPDATED_MINUTE = 2D;

    private static final Double DEFAULT_MEGABIT = 1D;
    private static final Double UPDATED_MEGABIT = 2D;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/gains";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GainsRepository gainsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGainsMockMvc;

    private Gains gains;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gains createEntity(EntityManager em) {
        Gains gains = new Gains()
            .telephone(DEFAULT_TELEPHONE)
            .minute(DEFAULT_MINUTE)
            .megabit(DEFAULT_MEGABIT)
            .createdAt(DEFAULT_CREATED_AT);
        return gains;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gains createUpdatedEntity(EntityManager em) {
        Gains gains = new Gains()
            .telephone(UPDATED_TELEPHONE)
            .minute(UPDATED_MINUTE)
            .megabit(UPDATED_MEGABIT)
            .createdAt(UPDATED_CREATED_AT);
        return gains;
    }

    @BeforeEach
    public void initTest() {
        gains = createEntity(em);
    }

    @Test
    @Transactional
    void createGains() throws Exception {
        int databaseSizeBeforeCreate = gainsRepository.findAll().size();
        // Create the Gains
        restGainsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gains)))
            .andExpect(status().isCreated());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeCreate + 1);
        Gains testGains = gainsList.get(gainsList.size() - 1);
        assertThat(testGains.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testGains.getMinute()).isEqualTo(DEFAULT_MINUTE);
        assertThat(testGains.getMegabit()).isEqualTo(DEFAULT_MEGABIT);
        assertThat(testGains.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void createGainsWithExistingId() throws Exception {
        // Create the Gains with an existing ID
        gains.setId(1L);

        int databaseSizeBeforeCreate = gainsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGainsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gains)))
            .andExpect(status().isBadRequest());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = gainsRepository.findAll().size();
        // set the field null
        gains.setTelephone(null);

        // Create the Gains, which fails.

        restGainsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gains)))
            .andExpect(status().isBadRequest());

        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinuteIsRequired() throws Exception {
        int databaseSizeBeforeTest = gainsRepository.findAll().size();
        // set the field null
        gains.setMinute(null);

        // Create the Gains, which fails.

        restGainsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gains)))
            .andExpect(status().isBadRequest());

        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMegabitIsRequired() throws Exception {
        int databaseSizeBeforeTest = gainsRepository.findAll().size();
        // set the field null
        gains.setMegabit(null);

        // Create the Gains, which fails.

        restGainsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gains)))
            .andExpect(status().isBadRequest());

        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = gainsRepository.findAll().size();
        // set the field null
        gains.setCreatedAt(null);

        // Create the Gains, which fails.

        restGainsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gains)))
            .andExpect(status().isBadRequest());

        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGains() throws Exception {
        // Initialize the database
        gainsRepository.saveAndFlush(gains);

        // Get all the gainsList
        restGainsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gains.getId().intValue())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].minute").value(hasItem(DEFAULT_MINUTE.doubleValue())))
            .andExpect(jsonPath("$.[*].megabit").value(hasItem(DEFAULT_MEGABIT.doubleValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))));
    }

    @Test
    @Transactional
    void getGains() throws Exception {
        // Initialize the database
        gainsRepository.saveAndFlush(gains);

        // Get the gains
        restGainsMockMvc
            .perform(get(ENTITY_API_URL_ID, gains.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gains.getId().intValue()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.minute").value(DEFAULT_MINUTE.doubleValue()))
            .andExpect(jsonPath("$.megabit").value(DEFAULT_MEGABIT.doubleValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)));
    }

    @Test
    @Transactional
    void getNonExistingGains() throws Exception {
        // Get the gains
        restGainsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGains() throws Exception {
        // Initialize the database
        gainsRepository.saveAndFlush(gains);

        int databaseSizeBeforeUpdate = gainsRepository.findAll().size();

        // Update the gains
        Gains updatedGains = gainsRepository.findById(gains.getId()).get();
        // Disconnect from session so that the updates on updatedGains are not directly saved in db
        em.detach(updatedGains);
        updatedGains.telephone(UPDATED_TELEPHONE).minute(UPDATED_MINUTE).megabit(UPDATED_MEGABIT).createdAt(UPDATED_CREATED_AT);

        restGainsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGains.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGains))
            )
            .andExpect(status().isOk());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeUpdate);
        Gains testGains = gainsList.get(gainsList.size() - 1);
        assertThat(testGains.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testGains.getMinute()).isEqualTo(UPDATED_MINUTE);
        assertThat(testGains.getMegabit()).isEqualTo(UPDATED_MEGABIT);
        assertThat(testGains.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingGains() throws Exception {
        int databaseSizeBeforeUpdate = gainsRepository.findAll().size();
        gains.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGainsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gains.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gains))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGains() throws Exception {
        int databaseSizeBeforeUpdate = gainsRepository.findAll().size();
        gains.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGainsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gains))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGains() throws Exception {
        int databaseSizeBeforeUpdate = gainsRepository.findAll().size();
        gains.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGainsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gains)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGainsWithPatch() throws Exception {
        // Initialize the database
        gainsRepository.saveAndFlush(gains);

        int databaseSizeBeforeUpdate = gainsRepository.findAll().size();

        // Update the gains using partial update
        Gains partialUpdatedGains = new Gains();
        partialUpdatedGains.setId(gains.getId());

        partialUpdatedGains.megabit(UPDATED_MEGABIT).createdAt(UPDATED_CREATED_AT);

        restGainsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGains.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGains))
            )
            .andExpect(status().isOk());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeUpdate);
        Gains testGains = gainsList.get(gainsList.size() - 1);
        assertThat(testGains.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testGains.getMinute()).isEqualTo(DEFAULT_MINUTE);
        assertThat(testGains.getMegabit()).isEqualTo(UPDATED_MEGABIT);
        assertThat(testGains.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateGainsWithPatch() throws Exception {
        // Initialize the database
        gainsRepository.saveAndFlush(gains);

        int databaseSizeBeforeUpdate = gainsRepository.findAll().size();

        // Update the gains using partial update
        Gains partialUpdatedGains = new Gains();
        partialUpdatedGains.setId(gains.getId());

        partialUpdatedGains.telephone(UPDATED_TELEPHONE).minute(UPDATED_MINUTE).megabit(UPDATED_MEGABIT).createdAt(UPDATED_CREATED_AT);

        restGainsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGains.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGains))
            )
            .andExpect(status().isOk());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeUpdate);
        Gains testGains = gainsList.get(gainsList.size() - 1);
        assertThat(testGains.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testGains.getMinute()).isEqualTo(UPDATED_MINUTE);
        assertThat(testGains.getMegabit()).isEqualTo(UPDATED_MEGABIT);
        assertThat(testGains.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingGains() throws Exception {
        int databaseSizeBeforeUpdate = gainsRepository.findAll().size();
        gains.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGainsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gains.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gains))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGains() throws Exception {
        int databaseSizeBeforeUpdate = gainsRepository.findAll().size();
        gains.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGainsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gains))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGains() throws Exception {
        int databaseSizeBeforeUpdate = gainsRepository.findAll().size();
        gains.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGainsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gains)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gains in the database
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGains() throws Exception {
        // Initialize the database
        gainsRepository.saveAndFlush(gains);

        int databaseSizeBeforeDelete = gainsRepository.findAll().size();

        // Delete the gains
        restGainsMockMvc
            .perform(delete(ENTITY_API_URL_ID, gains.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gains> gainsList = gainsRepository.findAll();
        assertThat(gainsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
