package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.Choix;
import com.techxel.play2win_admin.repository.ChoixRepository;
import com.techxel.play2win_admin.service.ChoixService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Choix}.
 */
@Service
@Transactional
public class ChoixServiceImpl implements ChoixService {

    private final Logger log = LoggerFactory.getLogger(ChoixServiceImpl.class);

    private final ChoixRepository choixRepository;

    public ChoixServiceImpl(ChoixRepository choixRepository) {
        this.choixRepository = choixRepository;
    }

    @Override
    public Choix save(Choix choix) {
        log.debug("Request to save Choix : {}", choix);
        return choixRepository.save(choix);
    }

    @Override
    public Choix update(Choix choix) {
        log.debug("Request to save Choix : {}", choix);
        // no save call needed as we have no fields that can be updated
        return choix;
    }

    @Override
    public Optional<Choix> partialUpdate(Choix choix) {
        log.debug("Request to partially update Choix : {}", choix);

        return choixRepository
            .findById(choix.getId())
            .map(existingChoix -> {
                return existingChoix;
            }); // .map(choixRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Choix> findAll(Pageable pageable) {
        log.debug("Request to get all Choixes");
        return choixRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Choix> findOne(Long id) {
        log.debug("Request to get Choix : {}", id);
        return choixRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Choix : {}", id);
        choixRepository.deleteById(id);
    }
}
