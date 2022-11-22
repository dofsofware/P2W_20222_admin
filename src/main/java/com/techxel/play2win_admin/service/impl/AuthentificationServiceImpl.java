package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.Authentification;
import com.techxel.play2win_admin.repository.AuthentificationRepository;
import com.techxel.play2win_admin.service.AuthentificationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Authentification}.
 */
@Service
@Transactional
public class AuthentificationServiceImpl implements AuthentificationService {

    private final Logger log = LoggerFactory.getLogger(AuthentificationServiceImpl.class);

    private final AuthentificationRepository authentificationRepository;

    public AuthentificationServiceImpl(AuthentificationRepository authentificationRepository) {
        this.authentificationRepository = authentificationRepository;
    }

    @Override
    public Authentification save(Authentification authentification) {
        log.debug("Request to save Authentification : {}", authentification);
        return authentificationRepository.save(authentification);
    }

    @Override
    public Authentification update(Authentification authentification) {
        log.debug("Request to save Authentification : {}", authentification);
        // no save call needed as we have no fields that can be updated
        return authentification;
    }

    @Override
    public Optional<Authentification> partialUpdate(Authentification authentification) {
        log.debug("Request to partially update Authentification : {}", authentification);

        return authentificationRepository
            .findById(authentification.getId())
            .map(existingAuthentification -> {
                return existingAuthentification;
            }); // .map(authentificationRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Authentification> findAll(Pageable pageable) {
        log.debug("Request to get all Authentifications");
        return authentificationRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Authentification> findOne(Long id) {
        log.debug("Request to get Authentification : {}", id);
        return authentificationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Authentification : {}", id);
        authentificationRepository.deleteById(id);
    }
}
