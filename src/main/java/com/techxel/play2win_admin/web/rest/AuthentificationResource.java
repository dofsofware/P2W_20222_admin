package com.techxel.play2win_admin.web.rest;

import com.techxel.play2win_admin.domain.Authentification;
import com.techxel.play2win_admin.repository.AuthentificationRepository;
import com.techxel.play2win_admin.service.AuthentificationService;
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
 * REST controller for managing {@link com.techxel.play2win_admin.domain.Authentification}.
 */
@RestController
@RequestMapping("/api")
public class AuthentificationResource {

    private final Logger log = LoggerFactory.getLogger(AuthentificationResource.class);

    private static final String ENTITY_NAME = "authentification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuthentificationService authentificationService;

    private final AuthentificationRepository authentificationRepository;

    public AuthentificationResource(
        AuthentificationService authentificationService,
        AuthentificationRepository authentificationRepository
    ) {
        this.authentificationService = authentificationService;
        this.authentificationRepository = authentificationRepository;
    }

    /**
     * {@code POST  /authentifications} : Create a new authentification.
     *
     * @param authentification the authentification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new authentification, or with status {@code 400 (Bad Request)} if the authentification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/authentifications")
    public ResponseEntity<Authentification> createAuthentification(@RequestBody Authentification authentification)
        throws URISyntaxException {
        log.debug("REST request to save Authentification : {}", authentification);
        if (authentification.getId() != null) {
            throw new BadRequestAlertException("A new authentification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Authentification result = authentificationService.save(authentification);
        return ResponseEntity
            .created(new URI("/api/authentifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /authentifications/:id} : Updates an existing authentification.
     *
     * @param id the id of the authentification to save.
     * @param authentification the authentification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authentification,
     * or with status {@code 400 (Bad Request)} if the authentification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the authentification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/authentifications/{id}")
    public ResponseEntity<Authentification> updateAuthentification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Authentification authentification
    ) throws URISyntaxException {
        log.debug("REST request to update Authentification : {}, {}", id, authentification);
        if (authentification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authentification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authentificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Authentification result = authentificationService.update(authentification);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authentification.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /authentifications/:id} : Partial updates given fields of an existing authentification, field will ignore if it is null
     *
     * @param id the id of the authentification to save.
     * @param authentification the authentification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated authentification,
     * or with status {@code 400 (Bad Request)} if the authentification is not valid,
     * or with status {@code 404 (Not Found)} if the authentification is not found,
     * or with status {@code 500 (Internal Server Error)} if the authentification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/authentifications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Authentification> partialUpdateAuthentification(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Authentification authentification
    ) throws URISyntaxException {
        log.debug("REST request to partial update Authentification partially : {}, {}", id, authentification);
        if (authentification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, authentification.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!authentificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Authentification> result = authentificationService.partialUpdate(authentification);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, authentification.getId().toString())
        );
    }

    /**
     * {@code GET  /authentifications} : get all the authentifications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authentifications in body.
     */
    @GetMapping("/authentifications")
    public ResponseEntity<List<Authentification>> getAllAuthentifications(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Authentifications");
        Page<Authentification> page = authentificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authentifications/:id} : get the "id" authentification.
     *
     * @param id the id of the authentification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authentification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/authentifications/{id}")
    public ResponseEntity<Authentification> getAuthentification(@PathVariable Long id) {
        log.debug("REST request to get Authentification : {}", id);
        Optional<Authentification> authentification = authentificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authentification);
    }

    /**
     * {@code DELETE  /authentifications/:id} : delete the "id" authentification.
     *
     * @param id the id of the authentification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/authentifications/{id}")
    public ResponseEntity<Void> deleteAuthentification(@PathVariable Long id) {
        log.debug("REST request to delete Authentification : {}", id);
        authentificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
