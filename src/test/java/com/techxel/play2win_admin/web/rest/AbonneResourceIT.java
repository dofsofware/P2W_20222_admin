package com.techxel.play2win_admin.web.rest;

import static com.techxel.play2win_admin.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techxel.play2win_admin.IntegrationTest;
import com.techxel.play2win_admin.domain.Abonne;
import com.techxel.play2win_admin.domain.enumeration.Niveau;
import com.techxel.play2win_admin.repository.AbonneRepository;
import com.techxel.play2win_admin.service.AbonneService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AbonneResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AbonneResourceIT {

    private static final String DEFAULT_IDENTIFIANT = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIANT = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBBBB";

    private static final String DEFAULT_MOT_DE_PASSE = "AAAAAAAAAA";
    private static final String UPDATED_MOT_DE_PASSE = "BBBBBBBBBB";

    private static final Double DEFAULT_SCORE = 1D;
    private static final Double UPDATED_SCORE = 2D;

    private static final Niveau DEFAULT_NIVEAU = Niveau.DEBUTANT;
    private static final Niveau UPDATED_NIVEAU = Niveau.INTERMEDIAIRE;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DERNIERE_PATICIPATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DERNIERE_PATICIPATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String DEFAULT_CODE_RACTIVATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_RACTIVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/abonnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AbonneRepository abonneRepository;

    @Mock
    private AbonneRepository abonneRepositoryMock;

    @Mock
    private AbonneService abonneServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAbonneMockMvc;

    private Abonne abonne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonne createEntity(EntityManager em) {
        Abonne abonne = new Abonne()
            .identifiant(DEFAULT_IDENTIFIANT)
            .telephone(DEFAULT_TELEPHONE)
            .motDePasse(DEFAULT_MOT_DE_PASSE)
            .score(DEFAULT_SCORE)
            .niveau(DEFAULT_NIVEAU)
            .createdAt(DEFAULT_CREATED_AT)
            .dernierePaticipation(DEFAULT_DERNIERE_PATICIPATION)
            .actif(DEFAULT_ACTIF)
            .codeRactivation(DEFAULT_CODE_RACTIVATION);
        return abonne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Abonne createUpdatedEntity(EntityManager em) {
        Abonne abonne = new Abonne()
            .identifiant(UPDATED_IDENTIFIANT)
            .telephone(UPDATED_TELEPHONE)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .score(UPDATED_SCORE)
            .niveau(UPDATED_NIVEAU)
            .createdAt(UPDATED_CREATED_AT)
            .dernierePaticipation(UPDATED_DERNIERE_PATICIPATION)
            .actif(UPDATED_ACTIF)
            .codeRactivation(UPDATED_CODE_RACTIVATION);
        return abonne;
    }

    @BeforeEach
    public void initTest() {
        abonne = createEntity(em);
    }

    @Test
    @Transactional
    void createAbonne() throws Exception {
        int databaseSizeBeforeCreate = abonneRepository.findAll().size();
        // Create the Abonne
        restAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isCreated());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeCreate + 1);
        Abonne testAbonne = abonneList.get(abonneList.size() - 1);
        assertThat(testAbonne.getIdentifiant()).isEqualTo(DEFAULT_IDENTIFIANT);
        assertThat(testAbonne.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testAbonne.getMotDePasse()).isEqualTo(DEFAULT_MOT_DE_PASSE);
        assertThat(testAbonne.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testAbonne.getNiveau()).isEqualTo(DEFAULT_NIVEAU);
        assertThat(testAbonne.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAbonne.getDernierePaticipation()).isEqualTo(DEFAULT_DERNIERE_PATICIPATION);
        assertThat(testAbonne.getActif()).isEqualTo(DEFAULT_ACTIF);
        assertThat(testAbonne.getCodeRactivation()).isEqualTo(DEFAULT_CODE_RACTIVATION);
    }

    @Test
    @Transactional
    void createAbonneWithExistingId() throws Exception {
        // Create the Abonne with an existing ID
        abonne.setId(1L);

        int databaseSizeBeforeCreate = abonneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentifiantIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonneRepository.findAll().size();
        // set the field null
        abonne.setIdentifiant(null);

        // Create the Abonne, which fails.

        restAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonneRepository.findAll().size();
        // set the field null
        abonne.setTelephone(null);

        // Create the Abonne, which fails.

        restAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMotDePasseIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonneRepository.findAll().size();
        // set the field null
        abonne.setMotDePasse(null);

        // Create the Abonne, which fails.

        restAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonneRepository.findAll().size();
        // set the field null
        abonne.setScore(null);

        // Create the Abonne, which fails.

        restAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNiveauIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonneRepository.findAll().size();
        // set the field null
        abonne.setNiveau(null);

        // Create the Abonne, which fails.

        restAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonneRepository.findAll().size();
        // set the field null
        abonne.setCreatedAt(null);

        // Create the Abonne, which fails.

        restAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        int databaseSizeBeforeTest = abonneRepository.findAll().size();
        // set the field null
        abonne.setActif(null);

        // Create the Abonne, which fails.

        restAbonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isBadRequest());

        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAbonnes() throws Exception {
        // Initialize the database
        abonneRepository.saveAndFlush(abonne);

        // Get all the abonneList
        restAbonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abonne.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifiant").value(hasItem(DEFAULT_IDENTIFIANT)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].motDePasse").value(hasItem(DEFAULT_MOT_DE_PASSE)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].dernierePaticipation").value(hasItem(sameInstant(DEFAULT_DERNIERE_PATICIPATION))))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].codeRactivation").value(hasItem(DEFAULT_CODE_RACTIVATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAbonnesWithEagerRelationshipsIsEnabled() throws Exception {
        when(abonneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAbonneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(abonneServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAbonnesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(abonneServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAbonneMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(abonneServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAbonne() throws Exception {
        // Initialize the database
        abonneRepository.saveAndFlush(abonne);

        // Get the abonne
        restAbonneMockMvc
            .perform(get(ENTITY_API_URL_ID, abonne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(abonne.getId().intValue()))
            .andExpect(jsonPath("$.identifiant").value(DEFAULT_IDENTIFIANT))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.motDePasse").value(DEFAULT_MOT_DE_PASSE))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE.doubleValue()))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU.toString()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.dernierePaticipation").value(sameInstant(DEFAULT_DERNIERE_PATICIPATION)))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()))
            .andExpect(jsonPath("$.codeRactivation").value(DEFAULT_CODE_RACTIVATION));
    }

    @Test
    @Transactional
    void getNonExistingAbonne() throws Exception {
        // Get the abonne
        restAbonneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAbonne() throws Exception {
        // Initialize the database
        abonneRepository.saveAndFlush(abonne);

        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();

        // Update the abonne
        Abonne updatedAbonne = abonneRepository.findById(abonne.getId()).get();
        // Disconnect from session so that the updates on updatedAbonne are not directly saved in db
        em.detach(updatedAbonne);
        updatedAbonne
            .identifiant(UPDATED_IDENTIFIANT)
            .telephone(UPDATED_TELEPHONE)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .score(UPDATED_SCORE)
            .niveau(UPDATED_NIVEAU)
            .createdAt(UPDATED_CREATED_AT)
            .dernierePaticipation(UPDATED_DERNIERE_PATICIPATION)
            .actif(UPDATED_ACTIF)
            .codeRactivation(UPDATED_CODE_RACTIVATION);

        restAbonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAbonne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAbonne))
            )
            .andExpect(status().isOk());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);
        Abonne testAbonne = abonneList.get(abonneList.size() - 1);
        assertThat(testAbonne.getIdentifiant()).isEqualTo(UPDATED_IDENTIFIANT);
        assertThat(testAbonne.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testAbonne.getMotDePasse()).isEqualTo(UPDATED_MOT_DE_PASSE);
        assertThat(testAbonne.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testAbonne.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testAbonne.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAbonne.getDernierePaticipation()).isEqualTo(UPDATED_DERNIERE_PATICIPATION);
        assertThat(testAbonne.getActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testAbonne.getCodeRactivation()).isEqualTo(UPDATED_CODE_RACTIVATION);
    }

    @Test
    @Transactional
    void putNonExistingAbonne() throws Exception {
        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();
        abonne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, abonne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAbonne() throws Exception {
        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();
        abonne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(abonne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAbonne() throws Exception {
        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();
        abonne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAbonneWithPatch() throws Exception {
        // Initialize the database
        abonneRepository.saveAndFlush(abonne);

        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();

        // Update the abonne using partial update
        Abonne partialUpdatedAbonne = new Abonne();
        partialUpdatedAbonne.setId(abonne.getId());

        partialUpdatedAbonne.score(UPDATED_SCORE).niveau(UPDATED_NIVEAU).createdAt(UPDATED_CREATED_AT).actif(UPDATED_ACTIF);

        restAbonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbonne))
            )
            .andExpect(status().isOk());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);
        Abonne testAbonne = abonneList.get(abonneList.size() - 1);
        assertThat(testAbonne.getIdentifiant()).isEqualTo(DEFAULT_IDENTIFIANT);
        assertThat(testAbonne.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testAbonne.getMotDePasse()).isEqualTo(DEFAULT_MOT_DE_PASSE);
        assertThat(testAbonne.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testAbonne.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testAbonne.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAbonne.getDernierePaticipation()).isEqualTo(DEFAULT_DERNIERE_PATICIPATION);
        assertThat(testAbonne.getActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testAbonne.getCodeRactivation()).isEqualTo(DEFAULT_CODE_RACTIVATION);
    }

    @Test
    @Transactional
    void fullUpdateAbonneWithPatch() throws Exception {
        // Initialize the database
        abonneRepository.saveAndFlush(abonne);

        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();

        // Update the abonne using partial update
        Abonne partialUpdatedAbonne = new Abonne();
        partialUpdatedAbonne.setId(abonne.getId());

        partialUpdatedAbonne
            .identifiant(UPDATED_IDENTIFIANT)
            .telephone(UPDATED_TELEPHONE)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .score(UPDATED_SCORE)
            .niveau(UPDATED_NIVEAU)
            .createdAt(UPDATED_CREATED_AT)
            .dernierePaticipation(UPDATED_DERNIERE_PATICIPATION)
            .actif(UPDATED_ACTIF)
            .codeRactivation(UPDATED_CODE_RACTIVATION);

        restAbonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAbonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAbonne))
            )
            .andExpect(status().isOk());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);
        Abonne testAbonne = abonneList.get(abonneList.size() - 1);
        assertThat(testAbonne.getIdentifiant()).isEqualTo(UPDATED_IDENTIFIANT);
        assertThat(testAbonne.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testAbonne.getMotDePasse()).isEqualTo(UPDATED_MOT_DE_PASSE);
        assertThat(testAbonne.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testAbonne.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testAbonne.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAbonne.getDernierePaticipation()).isEqualTo(UPDATED_DERNIERE_PATICIPATION);
        assertThat(testAbonne.getActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testAbonne.getCodeRactivation()).isEqualTo(UPDATED_CODE_RACTIVATION);
    }

    @Test
    @Transactional
    void patchNonExistingAbonne() throws Exception {
        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();
        abonne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, abonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(abonne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAbonne() throws Exception {
        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();
        abonne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(abonne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAbonne() throws Exception {
        int databaseSizeBeforeUpdate = abonneRepository.findAll().size();
        abonne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAbonneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(abonne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Abonne in the database
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAbonne() throws Exception {
        // Initialize the database
        abonneRepository.saveAndFlush(abonne);

        int databaseSizeBeforeDelete = abonneRepository.findAll().size();

        // Delete the abonne
        restAbonneMockMvc
            .perform(delete(ENTITY_API_URL_ID, abonne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Abonne> abonneList = abonneRepository.findAll();
        assertThat(abonneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
