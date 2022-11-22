package com.techxel.play2win_admin.web.rest;

import com.techxel.play2win_admin.domain.Principes;
import com.techxel.play2win_admin.repository.PrincipesRepository;
import com.techxel.play2win_admin.service.PrincipesService;
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
 * REST controller for managing {@link com.techxel.play2win_admin.domain.Principes}.
 */
@RestController
@RequestMapping("/api")
public class PrincipesResource {

    private final Logger log = LoggerFactory.getLogger(PrincipesResource.class);

    private static final String ENTITY_NAME = "principes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrincipesService principesService;

    private final PrincipesRepository principesRepository;

    public PrincipesResource(PrincipesService principesService, PrincipesRepository principesRepository) {
        this.principesService = principesService;
        this.principesRepository = principesRepository;
    }

    /**
     * {@code POST  /principes} : Create a new principes.
     *
     * @param principes the principes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new principes, or with status {@code 400 (Bad Request)} if the principes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/principes")
    public ResponseEntity<Principes> createPrincipes(@RequestBody Principes principes) throws URISyntaxException {
        log.debug("REST request to save Principes : {}", principes);
        if (principes.getId() != null) {
            throw new BadRequestAlertException("A new principes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Principes result = principesService.save(principes);
        return ResponseEntity
            .created(new URI("/api/principes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /principes/:id} : Updates an existing principes.
     *
     * @param id the id of the principes to save.
     * @param principes the principes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated principes,
     * or with status {@code 400 (Bad Request)} if the principes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the principes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/principes/{id}")
    public ResponseEntity<Principes> updatePrincipes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Principes principes
    ) throws URISyntaxException {
        log.debug("REST request to update Principes : {}, {}", id, principes);
        if (principes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, principes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!principesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Principes result = principesService.update(principes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, principes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /principes/:id} : Partial updates given fields of an existing principes, field will ignore if it is null
     *
     * @param id the id of the principes to save.
     * @param principes the principes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated principes,
     * or with status {@code 400 (Bad Request)} if the principes is not valid,
     * or with status {@code 404 (Not Found)} if the principes is not found,
     * or with status {@code 500 (Internal Server Error)} if the principes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/principes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Principes> partialUpdatePrincipes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Principes principes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Principes partially : {}, {}", id, principes);
        if (principes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, principes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!principesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Principes> result = principesService.partialUpdate(principes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, principes.getId().toString())
        );
    }

    /**
     * {@code GET  /principes} : get all the principes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of principes in body.
     */
    @GetMapping("/principes")
    public ResponseEntity<List<Principes>> getAllPrincipes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Principes");
        Page<Principes> page = principesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /principes/:id} : get the "id" principes.
     *
     * @param id the id of the principes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the principes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/principes/{id}")
    public ResponseEntity<Principes> getPrincipes(@PathVariable Long id) {
        log.debug("REST request to get Principes : {}", id);
        Optional<Principes> principes = principesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(principes);
    }

    /**
     * {@code DELETE  /principes/:id} : delete the "id" principes.
     *
     * @param id the id of the principes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/principes/{id}")
    public ResponseEntity<Void> deletePrincipes(@PathVariable Long id) {
        log.debug("REST request to delete Principes : {}", id);
        principesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
