package com.techxel.play2win_admin.web.rest;

import com.techxel.play2win_admin.domain.Gains;
import com.techxel.play2win_admin.repository.GainsRepository;
import com.techxel.play2win_admin.service.GainsService;
import com.techxel.play2win_admin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.techxel.play2win_admin.domain.Gains}.
 */
@RestController
@RequestMapping("/api")
public class GainsResource {

    private final Logger log = LoggerFactory.getLogger(GainsResource.class);

    private static final String ENTITY_NAME = "gains";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GainsService gainsService;

    private final GainsRepository gainsRepository;

    public GainsResource(GainsService gainsService, GainsRepository gainsRepository) {
        this.gainsService = gainsService;
        this.gainsRepository = gainsRepository;
    }

    /**
     * {@code POST  /gains} : Create a new gains.
     *
     * @param gains the gains to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gains, or with status {@code 400 (Bad Request)} if the gains has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gains")
    public ResponseEntity<Gains> createGains(@Valid @RequestBody Gains gains) throws URISyntaxException {
        log.debug("REST request to save Gains : {}", gains);
        if (gains.getId() != null) {
            throw new BadRequestAlertException("A new gains cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gains result = gainsService.save(gains);
        return ResponseEntity
            .created(new URI("/api/gains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gains/:id} : Updates an existing gains.
     *
     * @param id the id of the gains to save.
     * @param gains the gains to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gains,
     * or with status {@code 400 (Bad Request)} if the gains is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gains couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gains/{id}")
    public ResponseEntity<Gains> updateGains(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Gains gains)
        throws URISyntaxException {
        log.debug("REST request to update Gains : {}, {}", id, gains);
        if (gains.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gains.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gainsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Gains result = gainsService.update(gains);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gains.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gains/:id} : Partial updates given fields of an existing gains, field will ignore if it is null
     *
     * @param id the id of the gains to save.
     * @param gains the gains to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gains,
     * or with status {@code 400 (Bad Request)} if the gains is not valid,
     * or with status {@code 404 (Not Found)} if the gains is not found,
     * or with status {@code 500 (Internal Server Error)} if the gains couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gains/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Gains> partialUpdateGains(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Gains gains
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gains partially : {}, {}", id, gains);
        if (gains.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gains.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gainsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Gains> result = gainsService.partialUpdate(gains);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gains.getId().toString())
        );
    }

    /**
     * {@code GET  /gains} : get all the gains.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gains in body.
     */
    @GetMapping("/gains")
    public ResponseEntity<List<Gains>> getAllGains(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Gains");
        Page<Gains> page = gainsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gains/:id} : get the "id" gains.
     *
     * @param id the id of the gains to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gains, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gains/{id}")
    public ResponseEntity<Gains> getGains(@PathVariable Long id) {
        log.debug("REST request to get Gains : {}", id);
        Optional<Gains> gains = gainsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gains);
    }

    /**
     * {@code DELETE  /gains/:id} : delete the "id" gains.
     *
     * @param id the id of the gains to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gains/{id}")
    public ResponseEntity<Void> deleteGains(@PathVariable Long id) {
        log.debug("REST request to delete Gains : {}", id);
        gainsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
