package com.techxel.play2win_admin.web.rest;

import com.techxel.play2win_admin.domain.Restaure;
import com.techxel.play2win_admin.repository.RestaureRepository;
import com.techxel.play2win_admin.service.RestaureService;
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
 * REST controller for managing {@link com.techxel.play2win_admin.domain.Restaure}.
 */
@RestController
@RequestMapping("/api")
public class RestaureResource {

    private final Logger log = LoggerFactory.getLogger(RestaureResource.class);

    private static final String ENTITY_NAME = "restaure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaureService restaureService;

    private final RestaureRepository restaureRepository;

    public RestaureResource(RestaureService restaureService, RestaureRepository restaureRepository) {
        this.restaureService = restaureService;
        this.restaureRepository = restaureRepository;
    }

    /**
     * {@code POST  /restaures} : Create a new restaure.
     *
     * @param restaure the restaure to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaure, or with status {@code 400 (Bad Request)} if the restaure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaures")
    public ResponseEntity<Restaure> createRestaure(@RequestBody Restaure restaure) throws URISyntaxException {
        log.debug("REST request to save Restaure : {}", restaure);
        if (restaure.getId() != null) {
            throw new BadRequestAlertException("A new restaure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Restaure result = restaureService.save(restaure);
        return ResponseEntity
            .created(new URI("/api/restaures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaures/:id} : Updates an existing restaure.
     *
     * @param id the id of the restaure to save.
     * @param restaure the restaure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaure,
     * or with status {@code 400 (Bad Request)} if the restaure is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaures/{id}")
    public ResponseEntity<Restaure> updateRestaure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Restaure restaure
    ) throws URISyntaxException {
        log.debug("REST request to update Restaure : {}, {}", id, restaure);
        if (restaure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Restaure result = restaureService.update(restaure);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaure.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restaures/:id} : Partial updates given fields of an existing restaure, field will ignore if it is null
     *
     * @param id the id of the restaure to save.
     * @param restaure the restaure to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaure,
     * or with status {@code 400 (Bad Request)} if the restaure is not valid,
     * or with status {@code 404 (Not Found)} if the restaure is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaure couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restaures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Restaure> partialUpdateRestaure(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Restaure restaure
    ) throws URISyntaxException {
        log.debug("REST request to partial update Restaure partially : {}, {}", id, restaure);
        if (restaure.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaure.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Restaure> result = restaureService.partialUpdate(restaure);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaure.getId().toString())
        );
    }

    /**
     * {@code GET  /restaures} : get all the restaures.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaures in body.
     */
    @GetMapping("/restaures")
    public ResponseEntity<List<Restaure>> getAllRestaures(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Restaures");
        Page<Restaure> page = restaureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /restaures/:id} : get the "id" restaure.
     *
     * @param id the id of the restaure to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaure, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaures/{id}")
    public ResponseEntity<Restaure> getRestaure(@PathVariable Long id) {
        log.debug("REST request to get Restaure : {}", id);
        Optional<Restaure> restaure = restaureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restaure);
    }

    /**
     * {@code DELETE  /restaures/:id} : delete the "id" restaure.
     *
     * @param id the id of the restaure to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaures/{id}")
    public ResponseEntity<Void> deleteRestaure(@PathVariable Long id) {
        log.debug("REST request to delete Restaure : {}", id);
        restaureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
