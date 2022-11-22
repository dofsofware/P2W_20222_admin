package com.techxel.play2win_admin.web.rest;

import com.techxel.play2win_admin.domain.SaisieCode;
import com.techxel.play2win_admin.repository.SaisieCodeRepository;
import com.techxel.play2win_admin.service.SaisieCodeService;
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
 * REST controller for managing {@link com.techxel.play2win_admin.domain.SaisieCode}.
 */
@RestController
@RequestMapping("/api")
public class SaisieCodeResource {

    private final Logger log = LoggerFactory.getLogger(SaisieCodeResource.class);

    private static final String ENTITY_NAME = "saisieCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaisieCodeService saisieCodeService;

    private final SaisieCodeRepository saisieCodeRepository;

    public SaisieCodeResource(SaisieCodeService saisieCodeService, SaisieCodeRepository saisieCodeRepository) {
        this.saisieCodeService = saisieCodeService;
        this.saisieCodeRepository = saisieCodeRepository;
    }

    /**
     * {@code POST  /saisie-codes} : Create a new saisieCode.
     *
     * @param saisieCode the saisieCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saisieCode, or with status {@code 400 (Bad Request)} if the saisieCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/saisie-codes")
    public ResponseEntity<SaisieCode> createSaisieCode(@RequestBody SaisieCode saisieCode) throws URISyntaxException {
        log.debug("REST request to save SaisieCode : {}", saisieCode);
        if (saisieCode.getId() != null) {
            throw new BadRequestAlertException("A new saisieCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SaisieCode result = saisieCodeService.save(saisieCode);
        return ResponseEntity
            .created(new URI("/api/saisie-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /saisie-codes/:id} : Updates an existing saisieCode.
     *
     * @param id the id of the saisieCode to save.
     * @param saisieCode the saisieCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saisieCode,
     * or with status {@code 400 (Bad Request)} if the saisieCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saisieCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/saisie-codes/{id}")
    public ResponseEntity<SaisieCode> updateSaisieCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SaisieCode saisieCode
    ) throws URISyntaxException {
        log.debug("REST request to update SaisieCode : {}, {}", id, saisieCode);
        if (saisieCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saisieCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saisieCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SaisieCode result = saisieCodeService.update(saisieCode);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saisieCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /saisie-codes/:id} : Partial updates given fields of an existing saisieCode, field will ignore if it is null
     *
     * @param id the id of the saisieCode to save.
     * @param saisieCode the saisieCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saisieCode,
     * or with status {@code 400 (Bad Request)} if the saisieCode is not valid,
     * or with status {@code 404 (Not Found)} if the saisieCode is not found,
     * or with status {@code 500 (Internal Server Error)} if the saisieCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/saisie-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SaisieCode> partialUpdateSaisieCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SaisieCode saisieCode
    ) throws URISyntaxException {
        log.debug("REST request to partial update SaisieCode partially : {}, {}", id, saisieCode);
        if (saisieCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saisieCode.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saisieCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SaisieCode> result = saisieCodeService.partialUpdate(saisieCode);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saisieCode.getId().toString())
        );
    }

    /**
     * {@code GET  /saisie-codes} : get all the saisieCodes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of saisieCodes in body.
     */
    @GetMapping("/saisie-codes")
    public ResponseEntity<List<SaisieCode>> getAllSaisieCodes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SaisieCodes");
        Page<SaisieCode> page = saisieCodeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /saisie-codes/:id} : get the "id" saisieCode.
     *
     * @param id the id of the saisieCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saisieCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/saisie-codes/{id}")
    public ResponseEntity<SaisieCode> getSaisieCode(@PathVariable Long id) {
        log.debug("REST request to get SaisieCode : {}", id);
        Optional<SaisieCode> saisieCode = saisieCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saisieCode);
    }

    /**
     * {@code DELETE  /saisie-codes/:id} : delete the "id" saisieCode.
     *
     * @param id the id of the saisieCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/saisie-codes/{id}")
    public ResponseEntity<Void> deleteSaisieCode(@PathVariable Long id) {
        log.debug("REST request to delete SaisieCode : {}", id);
        saisieCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
