package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.MotDePasseSetting;
import com.techxel.play2win_admin.repository.MotDePasseSettingRepository;
import com.techxel.play2win_admin.service.MotDePasseSettingService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MotDePasseSetting}.
 */
@Service
@Transactional
public class MotDePasseSettingServiceImpl implements MotDePasseSettingService {

    private final Logger log = LoggerFactory.getLogger(MotDePasseSettingServiceImpl.class);

    private final MotDePasseSettingRepository motDePasseSettingRepository;

    public MotDePasseSettingServiceImpl(MotDePasseSettingRepository motDePasseSettingRepository) {
        this.motDePasseSettingRepository = motDePasseSettingRepository;
    }

    @Override
    public MotDePasseSetting save(MotDePasseSetting motDePasseSetting) {
        log.debug("Request to save MotDePasseSetting : {}", motDePasseSetting);
        return motDePasseSettingRepository.save(motDePasseSetting);
    }

    @Override
    public MotDePasseSetting update(MotDePasseSetting motDePasseSetting) {
        log.debug("Request to save MotDePasseSetting : {}", motDePasseSetting);
        // no save call needed as we have no fields that can be updated
        return motDePasseSetting;
    }

    @Override
    public Optional<MotDePasseSetting> partialUpdate(MotDePasseSetting motDePasseSetting) {
        log.debug("Request to partially update MotDePasseSetting : {}", motDePasseSetting);

        return motDePasseSettingRepository
            .findById(motDePasseSetting.getId())
            .map(existingMotDePasseSetting -> {
                return existingMotDePasseSetting;
            }); // .map(motDePasseSettingRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MotDePasseSetting> findAll(Pageable pageable) {
        log.debug("Request to get all MotDePasseSettings");
        return motDePasseSettingRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MotDePasseSetting> findOne(Long id) {
        log.debug("Request to get MotDePasseSetting : {}", id);
        return motDePasseSettingRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MotDePasseSetting : {}", id);
        motDePasseSettingRepository.deleteById(id);
    }
}
