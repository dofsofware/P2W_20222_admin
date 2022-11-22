package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.Play;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Play}.
 */
public interface PlayService {
    /**
     * Save a play.
     *
     * @param play the entity to save.
     * @return the persisted entity.
     */
    Play save(Play play);

    /**
     * Updates a play.
     *
     * @param play the entity to update.
     * @return the persisted entity.
     */
    Play update(Play play);

    /**
     * Partially updates a play.
     *
     * @param play the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Play> partialUpdate(Play play);

    /**
     * Get all the plays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Play> findAll(Pageable pageable);

    /**
     * Get the "id" play.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Play> findOne(Long id);

    /**
     * Delete the "id" play.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
