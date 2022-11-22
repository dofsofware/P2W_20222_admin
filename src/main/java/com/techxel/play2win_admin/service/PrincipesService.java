package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.Principes;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Principes}.
 */
public interface PrincipesService {
    /**
     * Save a principes.
     *
     * @param principes the entity to save.
     * @return the persisted entity.
     */
    Principes save(Principes principes);

    /**
     * Updates a principes.
     *
     * @param principes the entity to update.
     * @return the persisted entity.
     */
    Principes update(Principes principes);

    /**
     * Partially updates a principes.
     *
     * @param principes the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Principes> partialUpdate(Principes principes);

    /**
     * Get all the principes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Principes> findAll(Pageable pageable);

    /**
     * Get the "id" principes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Principes> findOne(Long id);

    /**
     * Delete the "id" principes.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
