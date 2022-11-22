package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.Authentification;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Authentification}.
 */
public interface AuthentificationService {
    /**
     * Save a authentification.
     *
     * @param authentification the entity to save.
     * @return the persisted entity.
     */
    Authentification save(Authentification authentification);

    /**
     * Updates a authentification.
     *
     * @param authentification the entity to update.
     * @return the persisted entity.
     */
    Authentification update(Authentification authentification);

    /**
     * Partially updates a authentification.
     *
     * @param authentification the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Authentification> partialUpdate(Authentification authentification);

    /**
     * Get all the authentifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Authentification> findAll(Pageable pageable);

    /**
     * Get the "id" authentification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Authentification> findOne(Long id);

    /**
     * Delete the "id" authentification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
