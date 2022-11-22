package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.Incription;
import com.techxel.play2win_admin.repository.IncriptionRepository;
import com.techxel.play2win_admin.service.IncriptionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Incription}.
 */
@Service
@Transactional
public class IncriptionServiceImpl implements IncriptionService {

    private final Logger log = LoggerFactory.getLogger(IncriptionServiceImpl.class);

    private final IncriptionRepository incriptionRepository;

    public IncriptionServiceImpl(IncriptionRepository incriptionRepository) {
        this.incriptionRepository = incriptionRepository;
    }

    @Override
    public Incription save(Incription incription) {
        log.debug("Request to save Incription : {}", incription);
        return incriptionRepository.save(incription);
    }

    @Override
    public Incription update(Incription incription) {
        log.debug("Request to save Incription : {}", incription);
        // no save call needed as we have no fields that can be updated
        return incription;
    }

    @Override
    public Optional<Incription> partialUpdate(Incription incription) {
        log.debug("Request to partially update Incription : {}", incription);

        return incriptionRepository
            .findById(incription.getId())
            .map(existingIncription -> {
                return existingIncription;
            }); // .map(incriptionRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Incription> findAll(Pageable pageable) {
        log.debug("Request to get all Incriptions");
        return incriptionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Incription> findOne(Long id) {
        log.debug("Request to get Incription : {}", id);
        return incriptionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Incription : {}", id);
        incriptionRepository.deleteById(id);
    }
}
