package com.techxel.play2win_admin.service;

import com.techxel.play2win_admin.domain.MotDePasseSetting;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link MotDePasseSetting}.
 */
public interface MotDePasseSettingService {
    /**
     * Save a motDePasseSetting.
     *
     * @param motDePasseSetting the entity to save.
     * @return the persisted entity.
     */
    MotDePasseSetting save(MotDePasseSetting motDePasseSetting);

    /**
     * Updates a motDePasseSetting.
     *
     * @param motDePasseSetting the entity to update.
     * @return the persisted entity.
     */
    MotDePasseSetting update(MotDePasseSetting motDePasseSetting);

    /**
     * Partially updates a motDePasseSetting.
     *
     * @param motDePasseSetting the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MotDePasseSetting> partialUpdate(MotDePasseSetting motDePasseSetting);

    /**
     * Get all the motDePasseSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MotDePasseSetting> findAll(Pageable pageable);

    /**
     * Get the "id" motDePasseSetting.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MotDePasseSetting> findOne(Long id);

    /**
     * Delete the "id" motDePasseSetting.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
