package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.Principes;
import com.techxel.play2win_admin.repository.PrincipesRepository;
import com.techxel.play2win_admin.service.PrincipesService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Principes}.
 */
@Service
@Transactional
public class PrincipesServiceImpl implements PrincipesService {

    private final Logger log = LoggerFactory.getLogger(PrincipesServiceImpl.class);

    private final PrincipesRepository principesRepository;

    public PrincipesServiceImpl(PrincipesRepository principesRepository) {
        this.principesRepository = principesRepository;
    }

    @Override
    public Principes save(Principes principes) {
        log.debug("Request to save Principes : {}", principes);
        return principesRepository.save(principes);
    }

    @Override
    public Principes update(Principes principes) {
        log.debug("Request to save Principes : {}", principes);
        // no save call needed as we have no fields that can be updated
        return principes;
    }

    @Override
    public Optional<Principes> partialUpdate(Principes principes) {
        log.debug("Request to partially update Principes : {}", principes);

        return principesRepository
            .findById(principes.getId())
            .map(existingPrincipes -> {
                return existingPrincipes;
            }); // .map(principesRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Principes> findAll(Pageable pageable) {
        log.debug("Request to get all Principes");
        return principesRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Principes> findOne(Long id) {
        log.debug("Request to get Principes : {}", id);
        return principesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Principes : {}", id);
        principesRepository.deleteById(id);
    }
}
