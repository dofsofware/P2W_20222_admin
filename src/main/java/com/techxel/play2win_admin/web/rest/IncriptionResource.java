package com.techxel.play2win_admin.web.rest;

import com.techxel.play2win_admin.domain.Incription;
import com.techxel.play2win_admin.repository.IncriptionRepository;
import com.techxel.play2win_admin.service.IncriptionService;
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
 * REST controller for managing {@link com.techxel.play2win_admin.domain.Incription}.
 */
@RestController
@RequestMapping("/api")
public class IncriptionResource {

    private final Logger log = LoggerFactory.getLogger(IncriptionResource.class);

    private static final String ENTITY_NAME = "incription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IncriptionService incriptionService;

    private final IncriptionRepository incriptionRepository;

    public IncriptionResource(IncriptionService incriptionService, IncriptionRepository incriptionRepository) {
        this.incriptionService = incriptionService;
        this.incriptionRepository = incriptionRepository;
    }

    /**
     * {@code POST  /incriptions} : Create a new incription.
     *
     * @param incription the incription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new incription, or with status {@code 400 (Bad Request)} if the incription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/incriptions")
    public ResponseEntity<Incription> createIncription(@RequestBody Incription incription) throws URISyntaxException {
        log.debug("REST request to save Incription : {}", incription);
        if (incription.getId() != null) {
            throw new BadRequestAlertException("A new incription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Incription result = incriptionService.save(incription);
        return ResponseEntity
            .created(new URI("/api/incriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /incriptions/:id} : Updates an existing incription.
     *
     * @param id the id of the incription to save.
     * @param incription the incription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incription,
     * or with status {@code 400 (Bad Request)} if the incription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the incription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/incriptions/{id}")
    public ResponseEntity<Incription> updateIncription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Incription incription
    ) throws URISyntaxException {
        log.debug("REST request to update Incription : {}, {}", id, incription);
        if (incription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Incription result = incriptionService.update(incription);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incription.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /incriptions/:id} : Partial updates given fields of an existing incription, field will ignore if it is null
     *
     * @param id the id of the incription to save.
     * @param incription the incription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated incription,
     * or with status {@code 400 (Bad Request)} if the incription is not valid,
     * or with status {@code 404 (Not Found)} if the incription is not found,
     * or with status {@code 500 (Internal Server Error)} if the incription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/incriptions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Incription> partialUpdateIncription(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Incription incription
    ) throws URISyntaxException {
        log.debug("REST request to partial update Incription partially : {}, {}", id, incription);
        if (incription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, incription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!incriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Incription> result = incriptionService.partialUpdate(incription);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, incription.getId().toString())
        );
    }

    /**
     * {@code GET  /incriptions} : get all the incriptions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of incriptions in body.
     */
    @GetMapping("/incriptions")
    public ResponseEntity<List<Incription>> getAllIncriptions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Incriptions");
        Page<Incription> page = incriptionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /incriptions/:id} : get the "id" incription.
     *
     * @param id the id of the incription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the incription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/incriptions/{id}")
    public ResponseEntity<Incription> getIncription(@PathVariable Long id) {
        log.debug("REST request to get Incription : {}", id);
        Optional<Incription> incription = incriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(incription);
    }

    /**
     * {@code DELETE  /incriptions/:id} : delete the "id" incription.
     *
     * @param id the id of the incription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/incriptions/{id}")
    public ResponseEntity<Void> deleteIncription(@PathVariable Long id) {
        log.debug("REST request to delete Incription : {}", id);
        incriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
