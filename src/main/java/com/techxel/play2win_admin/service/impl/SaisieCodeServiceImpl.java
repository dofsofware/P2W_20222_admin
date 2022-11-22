package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.SaisieCode;
import com.techxel.play2win_admin.repository.SaisieCodeRepository;
import com.techxel.play2win_admin.service.SaisieCodeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SaisieCode}.
 */
@Service
@Transactional
public class SaisieCodeServiceImpl implements SaisieCodeService {

    private final Logger log = LoggerFactory.getLogger(SaisieCodeServiceImpl.class);

    private final SaisieCodeRepository saisieCodeRepository;

    public SaisieCodeServiceImpl(SaisieCodeRepository saisieCodeRepository) {
        this.saisieCodeRepository = saisieCodeRepository;
    }

    @Override
    public SaisieCode save(SaisieCode saisieCode) {
        log.debug("Request to save SaisieCode : {}", saisieCode);
        return saisieCodeRepository.save(saisieCode);
    }

    @Override
    public SaisieCode update(SaisieCode saisieCode) {
        log.debug("Request to save SaisieCode : {}", saisieCode);
        // no save call needed as we have no fields that can be updated
        return saisieCode;
    }

    @Override
    public Optional<SaisieCode> partialUpdate(SaisieCode saisieCode) {
        log.debug("Request to partially update SaisieCode : {}", saisieCode);

        return saisieCodeRepository
            .findById(saisieCode.getId())
            .map(existingSaisieCode -> {
                return existingSaisieCode;
            }); // .map(saisieCodeRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SaisieCode> findAll(Pageable pageable) {
        log.debug("Request to get all SaisieCodes");
        return saisieCodeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SaisieCode> findOne(Long id) {
        log.debug("Request to get SaisieCode : {}", id);
        return saisieCodeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SaisieCode : {}", id);
        saisieCodeRepository.deleteById(id);
    }
}
