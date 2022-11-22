package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.Gains;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Gains}.
 */
public interface GainsService {
    /**
     * Save a gains.
     *
     * @param gains the entity to save.
     * @return the persisted entity.
     */
    Gains save(Gains gains);

    /**
     * Updates a gains.
     *
     * @param gains the entity to update.
     * @return the persisted entity.
     */
    Gains update(Gains gains);

    /**
     * Partially updates a gains.
     *
     * @param gains the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Gains> partialUpdate(Gains gains);

    /**
     * Get all the gains.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Gains> findAll(Pageable pageable);

    /**
     * Get the "id" gains.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Gains> findOne(Long id);

    /**
     * Delete the "id" gains.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
