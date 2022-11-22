package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.Resultat;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Resultat}.
 */
public interface ResultatService {
    /**
     * Save a resultat.
     *
     * @param resultat the entity to save.
     * @return the persisted entity.
     */
    Resultat save(Resultat resultat);

    /**
     * Updates a resultat.
     *
     * @param resultat the entity to update.
     * @return the persisted entity.
     */
    Resultat update(Resultat resultat);

    /**
     * Partially updates a resultat.
     *
     * @param resultat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Resultat> partialUpdate(Resultat resultat);

    /**
     * Get all the resultats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Resultat> findAll(Pageable pageable);

    /**
     * Get the "id" resultat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Resultat> findOne(Long id);

    /**
     * Delete the "id" resultat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
