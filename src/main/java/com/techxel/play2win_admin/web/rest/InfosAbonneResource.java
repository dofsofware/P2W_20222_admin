package com.techxel.play2win_admin.web.rest;

import com.techxel.play2win_admin.domain.InfosAbonne;
import com.techxel.play2win_admin.repository.InfosAbonneRepository;
import com.techxel.play2win_admin.service.InfosAbonneService;
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
 * REST controller for managing {@link com.techxel.play2win_admin.domain.InfosAbonne}.
 */
@RestController
@RequestMapping("/api")
public class InfosAbonneResource {

    private final Logger log = LoggerFactory.getLogger(InfosAbonneResource.class);

    private static final String ENTITY_NAME = "infosAbonne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InfosAbonneService infosAbonneService;

    private final InfosAbonneRepository infosAbonneRepository;

    public InfosAbonneResource(InfosAbonneService infosAbonneService, InfosAbonneRepository infosAbonneRepository) {
        this.infosAbonneService = infosAbonneService;
        this.infosAbonneRepository = infosAbonneRepository;
    }

    /**
     * {@code POST  /infos-abonnes} : Create a new infosAbonne.
     *
     * @param infosAbonne the infosAbonne to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new infosAbonne, or with status {@code 400 (Bad Request)} if the infosAbonne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/infos-abonnes")
    public ResponseEntity<InfosAbonne> createInfosAbonne(@RequestBody InfosAbonne infosAbonne) throws URISyntaxException {
        log.debug("REST request to save InfosAbonne : {}", infosAbonne);
        if (infosAbonne.getId() != null) {
            throw new BadRequestAlertException("A new infosAbonne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InfosAbonne result = infosAbonneService.save(infosAbonne);
        return ResponseEntity
            .created(new URI("/api/infos-abonnes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /infos-abonnes/:id} : Updates an existing infosAbonne.
     *
     * @param id the id of the infosAbonne to save.
     * @param infosAbonne the infosAbonne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infosAbonne,
     * or with status {@code 400 (Bad Request)} if the infosAbonne is not valid,
     * or with status {@code 500 (Internal Server Error)} if the infosAbonne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/infos-abonnes/{id}")
    public ResponseEntity<InfosAbonne> updateInfosAbonne(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InfosAbonne infosAbonne
    ) throws URISyntaxException {
        log.debug("REST request to update InfosAbonne : {}, {}", id, infosAbonne);
        if (infosAbonne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infosAbonne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infosAbonneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InfosAbonne result = infosAbonneService.update(infosAbonne);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, infosAbonne.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /infos-abonnes/:id} : Partial updates given fields of an existing infosAbonne, field will ignore if it is null
     *
     * @param id the id of the infosAbonne to save.
     * @param infosAbonne the infosAbonne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated infosAbonne,
     * or with status {@code 400 (Bad Request)} if the infosAbonne is not valid,
     * or with status {@code 404 (Not Found)} if the infosAbonne is not found,
     * or with status {@code 500 (Internal Server Error)} if the infosAbonne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/infos-abonnes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InfosAbonne> partialUpdateInfosAbonne(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InfosAbonne infosAbonne
    ) throws URISyntaxException {
        log.debug("REST request to partial update InfosAbonne partially : {}, {}", id, infosAbonne);
        if (infosAbonne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, infosAbonne.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!infosAbonneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InfosAbonne> result = infosAbonneService.partialUpdate(infosAbonne);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, infosAbonne.getId().toString())
        );
    }

    /**
     * {@code GET  /infos-abonnes} : get all the infosAbonnes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of infosAbonnes in body.
     */
    @GetMapping("/infos-abonnes")
    public ResponseEntity<List<InfosAbonne>> getAllInfosAbonnes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of InfosAbonnes");
        Page<InfosAbonne> page = infosAbonneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /infos-abonnes/:id} : get the "id" infosAbonne.
     *
     * @param id the id of the infosAbonne to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the infosAbonne, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/infos-abonnes/{id}")
    public ResponseEntity<InfosAbonne> getInfosAbonne(@PathVariable Long id) {
        log.debug("REST request to get InfosAbonne : {}", id);
        Optional<InfosAbonne> infosAbonne = infosAbonneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(infosAbonne);
    }

    /**
     * {@code DELETE  /infos-abonnes/:id} : delete the "id" infosAbonne.
     *
     * @param id the id of the infosAbonne to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/infos-abonnes/{id}")
    public ResponseEntity<Void> deleteInfosAbonne(@PathVariable Long id) {
        log.debug("REST request to delete InfosAbonne : {}", id);
        infosAbonneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
