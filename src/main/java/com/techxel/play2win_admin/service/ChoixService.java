package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.Choix;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Choix}.
 */
public interface ChoixService {
    /**
     * Save a choix.
     *
     * @param choix the entity to save.
     * @return the persisted entity.
     */
    Choix save(Choix choix);

    /**
     * Updates a choix.
     *
     * @param choix the entity to update.
     * @return the persisted entity.
     */
    Choix update(Choix choix);

    /**
     * Partially updates a choix.
     *
     * @param choix the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Choix> partialUpdate(Choix choix);

    /**
     * Get all the choixes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Choix> findAll(Pageable pageable);

    /**
     * Get the "id" choix.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Choix> findOne(Long id);

    /**
     * Delete the "id" choix.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
