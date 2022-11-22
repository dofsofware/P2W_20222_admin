package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.InfosAbonne;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link InfosAbonne}.
 */
public interface InfosAbonneService {
    /**
     * Save a infosAbonne.
     *
     * @param infosAbonne the entity to save.
     * @return the persisted entity.
     */
    InfosAbonne save(InfosAbonne infosAbonne);

    /**
     * Updates a infosAbonne.
     *
     * @param infosAbonne the entity to update.
     * @return the persisted entity.
     */
    InfosAbonne update(InfosAbonne infosAbonne);

    /**
     * Partially updates a infosAbonne.
     *
     * @param infosAbonne the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InfosAbonne> partialUpdate(InfosAbonne infosAbonne);

    /**
     * Get all the infosAbonnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InfosAbonne> findAll(Pageable pageable);

    /**
     * Get the "id" infosAbonne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InfosAbonne> findOne(Long id);

    /**
     * Delete the "id" infosAbonne.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
