package com.techxel.play2win_admin.web.rest;

import com.techxel.play2win_admin.domain.MotDePasseSetting;
import com.techxel.play2win_admin.repository.MotDePasseSettingRepository;
import com.techxel.play2win_admin.service.MotDePasseSettingService;
import com.techxel.play2win_admin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.techxel.play2win_admin.domain.MotDePasseSetting}.
 */
@RestController
@RequestMapping("/api")
public class MotDePasseSettingResource {

    private final Logger log = LoggerFactory.getLogger(MotDePasseSettingResource.class);

    private static final String ENTITY_NAME = "motDePasseSetting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MotDePasseSettingService motDePasseSettingService;

    private final MotDePasseSettingRepository motDePasseSettingRepository;

    public MotDePasseSettingResource(
        MotDePasseSettingService motDePasseSettingService,
        MotDePasseSettingRepository motDePasseSettingRepository
    ) {
        this.motDePasseSettingService = motDePasseSettingService;
        this.motDePasseSettingRepository = motDePasseSettingRepository;
    }

    /**
     * {@code POST  /mot-de-passe-settings} : Create a new motDePasseSetting.
     *
     * @param motDePasseSetting the motDePasseSetting to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new motDePasseSetting, or with status {@code 400 (Bad Request)} if the motDePasseSetting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mot-de-passe-settings")
    public ResponseEntity<MotDePasseSetting> createMotDePasseSetting(@RequestBody MotDePasseSetting motDePasseSetting)
        throws URISyntaxException {
        log.debug("REST request to save MotDePasseSetting : {}", motDePasseSetting);
        if (motDePasseSetting.getId() != null) {
            throw new BadRequestAlertException("A new motDePasseSetting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MotDePasseSetting result = motDePasseSettingService.save(motDePasseSetting);
        return ResponseEntity
            .created(new URI("/api/mot-de-passe-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mot-de-passe-settings/:id} : Updates an existing motDePasseSetting.
     *
     * @param id the id of the motDePasseSetting to save.
     * @param motDePasseSetting the motDePasseSetting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motDePasseSetting,
     * or with status {@code 400 (Bad Request)} if the motDePasseSetting is not valid,
     * or with status {@code 500 (Internal Server Error)} if the motDePasseSetting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mot-de-passe-settings/{id}")
    public ResponseEntity<MotDePasseSetting> updateMotDePasseSetting(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MotDePasseSetting motDePasseSetting
    ) throws URISyntaxException {
        log.debug("REST request to update MotDePasseSetting : {}, {}", id, motDePasseSetting);
        if (motDePasseSetting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, motDePasseSetting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motDePasseSettingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MotDePasseSetting result = motDePasseSettingService.update(motDePasseSetting);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, motDePasseSetting.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mot-de-passe-settings/:id} : Partial updates given fields of an existing motDePasseSetting, field will ignore if it is null
     *
     * @param id the id of the motDePasseSetting to save.
     * @param motDePasseSetting the motDePasseSetting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated motDePasseSetting,
     * or with status {@code 400 (Bad Request)} if the motDePasseSetting is not valid,
     * or with status {@code 404 (Not Found)} if the motDePasseSetting is not found,
     * or with status {@code 500 (Internal Server Error)} if the motDePasseSetting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mot-de-passe-settings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MotDePasseSetting> partialUpdateMotDePasseSetting(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MotDePasseSetting motDePasseSetting
    ) throws URISyntaxException {
        log.debug("REST request to partial update MotDePasseSetting partially : {}, {}", id, motDePasseSetting);
        if (motDePasseSetting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, motDePasseSetting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!motDePasseSettingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MotDePasseSetting> result = motDePasseSettingService.partialUpdate(motDePasseSetting);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, motDePasseSetting.getId().toString())
        );
    }

    /**
     * {@code GET  /mot-de-passe-settings} : get all the motDePasseSettings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of motDePasseSettings in body.
     */
    @GetMapping("/mot-de-passe-settings")
    public ResponseEntity<List<MotDePasseSetting>> getAllMotDePasseSettings(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of MotDePasseSettings");
        Page<MotDePasseSetting> page = motDePasseSettingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mot-de-passe-settings/:id} : get the "id" motDePasseSetting.
     *
     * @param id the id of the motDePasseSetting to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the motDePasseSetting, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mot-de-passe-settings/{id}")
    public ResponseEntity<MotDePasseSetting> getMotDePasseSetting(@PathVariable Long id) {
        log.debug("REST request to get MotDePasseSetting : {}", id);
        Optional<MotDePasseSetting> motDePasseSetting = motDePasseSettingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(motDePasseSetting);
    }

    /**
     * {@code DELETE  /mot-de-passe-settings/:id} : delete the "id" motDePasseSetting.
     *
     * @param id the id of the motDePasseSetting to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mot-de-passe-settings/{id}")
    public ResponseEntity<Void> deleteMotDePasseSetting(@PathVariable Long id) {
        log.debug("REST request to delete MotDePasseSetting : {}", id);
        motDePasseSettingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
