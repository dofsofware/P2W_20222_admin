package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.Abonne;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Abonne}.
 */
public interface AbonneService {
    /**
     * Save a abonne.
     *
     * @param abonne the entity to save.
     * @return the persisted entity.
     */
    Abonne save(Abonne abonne);

    /**
     * Updates a abonne.
     *
     * @param abonne the entity to update.
     * @return the persisted entity.
     */
    Abonne update(Abonne abonne);

    /**
     * Partially updates a abonne.
     *
     * @param abonne the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Abonne> partialUpdate(Abonne abonne);

    /**
     * Get all the abonnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Abonne> findAll(Pageable pageable);

    /**
     * Get all the abonnes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Abonne> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" abonne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Abonne> findOne(Long id);

    /**
     * Delete the "id" abonne.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
