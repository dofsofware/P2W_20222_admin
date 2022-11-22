package com.techxel.play2win_admin.web.rest;

import com.techxel.play2win_admin.domain.Abonne;
import com.techxel.play2win_admin.repository.AbonneRepository;
import com.techxel.play2win_admin.service.AbonneService;
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
 * REST controller for managing {@link com.techxel.play2win_admin.domain.Abonne}.
 */
@RestController
@RequestMapping("/api")
public class AbonneResource {

    private final Logger log = LoggerFactory.getLogger(AbonneResource.class);

    private static final String ENTITY_NAME = "abonne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AbonneService abonneService;

    private final AbonneRepository abonneRepository;

    public AbonneResource(AbonneService abonneService, AbonneRepository abonneRepository) {
        this.abonneService = abonneService;
        this.abonneRepository = abonneRepository;
    }

    /**
     * {@code POST  /abonnes} : Create a new abonne.
     *
     * @param abonne the abonne to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new abonne, or with status {@code 400 (Bad Request)} if the abonne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/abonnes")
    public ResponseEntity<Abonne> createAbonne(@Valid @RequestBody Abonne abonne) throws URISyntaxException {
        log.debug("REST request to save Abonne : {}", abonne);
        if (abonne.getId() != null) {
            throw new BadRequestAlertException("A new abonne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Abonne result = abonneService.save(abonne);
        return ResponseEntity
            .created(new URI("/api/abonnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /abonnes/:id} : Updates an existing abonne.
     *
     * @param id the id of the abonne to save.
     * @param abonne the abonne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated abonne,
     * or with status {@code 400 (Bad Request)} if the abonne is not valid,
     * or with status {@code 500 (Internal Server Error)} if the abonne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/abonnes/{id}")
    public ResponseEntity<Abonne> updateAbonne(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Abonne abonne
    ) throws URISyntaxException {
        log.debug("REST request to update Abonne : {}, {}", id, abonne);
        if (abonne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, abonne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!abonneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Abonne result = abonneService.update(abonne);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, abonne.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /abonnes/:id} : Partial updates given fields of an existing abonne, field will ignore if it is null
     *
     * @param id the id of the abonne to save.
     * @param abonne the abonne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated abonne,
     * or with status {@code 400 (Bad Request)} if the abonne is not valid,
     * or with status {@code 404 (Not Found)} if the abonne is not found,
     * or with status {@code 500 (Internal Server Error)} if the abonne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/abonnes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Abonne> partialUpdateAbonne(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Abonne abonne
    ) throws URISyntaxException {
        log.debug("REST request to partial update Abonne partially : {}, {}", id, abonne);
        if (abonne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, abonne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!abonneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Abonne> result = abonneService.partialUpdate(abonne);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, abonne.getId().toString())
        );
    }

    /**
     * {@code GET  /abonnes} : get all the abonnes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of abonnes in body.
     */
    @GetMapping("/abonnes")
    public ResponseEntity<List<Abonne>> getAllAbonnes(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Abonnes");
        Page<Abonne> page;
        if (eagerload) {
            page = abonneService.findAllWithEagerRelationships(pageable);
        } else {
            page = abonneService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /abonnes/:id} : get the "id" abonne.
     *
     * @param id the id of the abonne to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the abonne, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/abonnes/{id}")
    public ResponseEntity<Abonne> getAbonne(@PathVariable Long id) {
        log.debug("REST request to get Abonne : {}", id);
        Optional<Abonne> abonne = abonneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(abonne);
    }

    /**
     * {@code DELETE  /abonnes/:id} : delete the "id" abonne.
     *
     * @param id the id of the abonne to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/abonnes/{id}")
    public ResponseEntity<Void> deleteAbonne(@PathVariable Long id) {
        log.debug("REST request to delete Abonne : {}", id);
        abonneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
