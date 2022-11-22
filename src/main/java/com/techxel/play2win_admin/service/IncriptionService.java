package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.Incription;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Incription}.
 */
public interface IncriptionService {
    /**
     * Save a incription.
     *
     * @param incription the entity to save.
     * @return the persisted entity.
     */
    Incription save(Incription incription);

    /**
     * Updates a incription.
     *
     * @param incription the entity to update.
     * @return the persisted entity.
     */
    Incription update(Incription incription);

    /**
     * Partially updates a incription.
     *
     * @param incription the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Incription> partialUpdate(Incription incription);

    /**
     * Get all the incriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Incription> findAll(Pageable pageable);

    /**
     * Get the "id" incription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Incription> findOne(Long id);

    /**
     * Delete the "id" incription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
