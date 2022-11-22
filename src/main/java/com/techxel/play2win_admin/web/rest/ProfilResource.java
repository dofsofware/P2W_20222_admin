package com.techxel.play2win_admin.web.rest;

import com.techxel.play2win_admin.domain.Profil;
import com.techxel.play2win_admin.repository.ProfilRepository;
import com.techxel.play2win_admin.service.ProfilService;
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
 * REST controller for managing {@link com.techxel.play2win_admin.domain.Profil}.
 */
@RestController
@RequestMapping("/api")
public class ProfilResource {

    private final Logger log = LoggerFactory.getLogger(ProfilResource.class);

    private static final String ENTITY_NAME = "profil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfilService profilService;

    private final ProfilRepository profilRepository;

    public ProfilResource(ProfilService profilService, ProfilRepository profilRepository) {
        this.profilService = profilService;
        this.profilRepository = profilRepository;
    }

    /**
     * {@code POST  /profils} : Create a new profil.
     *
     * @param profil the profil to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profil, or with status {@code 400 (Bad Request)} if the profil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/profils")
    public ResponseEntity<Profil> createProfil(@RequestBody Profil profil) throws URISyntaxException {
        log.debug("REST request to save Profil : {}", profil);
        if (profil.getId() != null) {
            throw new BadRequestAlertException("A new profil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Profil result = profilService.save(profil);
        return ResponseEntity
            .created(new URI("/api/profils/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /profils/:id} : Updates an existing profil.
     *
     * @param id the id of the profil to save.
     * @param profil the profil to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profil,
     * or with status {@code 400 (Bad Request)} if the profil is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profil couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/profils/{id}")
    public ResponseEntity<Profil> updateProfil(@PathVariable(value = "id", required = false) final Long id, @RequestBody Profil profil)
        throws URISyntaxException {
        log.debug("REST request to update Profil : {}, {}", id, profil);
        if (profil.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profil.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Profil result = profilService.update(profil);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profil.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /profils/:id} : Partial updates given fields of an existing profil, field will ignore if it is null
     *
     * @param id the id of the profil to save.
     * @param profil the profil to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profil,
     * or with status {@code 400 (Bad Request)} if the profil is not valid,
     * or with status {@code 404 (Not Found)} if the profil is not found,
     * or with status {@code 500 (Internal Server Error)} if the profil couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/profils/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Profil> partialUpdateProfil(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Profil profil
    ) throws URISyntaxException {
        log.debug("REST request to partial update Profil partially : {}, {}", id, profil);
        if (profil.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profil.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Profil> result = profilService.partialUpdate(profil);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profil.getId().toString())
        );
    }

    /**
     * {@code GET  /profils} : get all the profils.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profils in body.
     */
    @GetMapping("/profils")
    public ResponseEntity<List<Profil>> getAllProfils(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Profils");
        Page<Profil> page = profilService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /profils/:id} : get the "id" profil.
     *
     * @param id the id of the profil to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profil, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/profils/{id}")
    public ResponseEntity<Profil> getProfil(@PathVariable Long id) {
        log.debug("REST request to get Profil : {}", id);
        Optional<Profil> profil = profilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profil);
    }

    /**
     * {@code DELETE  /profils/:id} : delete the "id" profil.
     *
     * @param id the id of the profil to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/profils/{id}")
    public ResponseEntity<Void> deleteProfil(@PathVariable Long id) {
        log.debug("REST request to delete Profil : {}", id);
        profilService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
