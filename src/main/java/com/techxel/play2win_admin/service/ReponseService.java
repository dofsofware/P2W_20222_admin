package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.Reponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Reponse}.
 */
public interface ReponseService {
    /**
     * Save a reponse.
     *
     * @param reponse the entity to save.
     * @return the persisted entity.
     */
    Reponse save(Reponse reponse);

    /**
     * Updates a reponse.
     *
     * @param reponse the entity to update.
     * @return the persisted entity.
     */
    Reponse update(Reponse reponse);

    /**
     * Partially updates a reponse.
     *
     * @param reponse the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Reponse> partialUpdate(Reponse reponse);

    /**
     * Get all the reponses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Reponse> findAll(Pageable pageable);

    /**
     * Get the "id" reponse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Reponse> findOne(Long id);

    /**
     * Delete the "id" reponse.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
