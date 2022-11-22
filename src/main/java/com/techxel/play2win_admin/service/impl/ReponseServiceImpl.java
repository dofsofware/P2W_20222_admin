package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.Reponse;
import com.techxel.play2win_admin.repository.ReponseRepository;
import com.techxel.play2win_admin.service.ReponseService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reponse}.
 */
@Service
@Transactional
public class ReponseServiceImpl implements ReponseService {

    private final Logger log = LoggerFactory.getLogger(ReponseServiceImpl.class);

    private final ReponseRepository reponseRepository;

    public ReponseServiceImpl(ReponseRepository reponseRepository) {
        this.reponseRepository = reponseRepository;
    }

    @Override
    public Reponse save(Reponse reponse) {
        log.debug("Request to save Reponse : {}", reponse);
        return reponseRepository.save(reponse);
    }

    @Override
    public Reponse update(Reponse reponse) {
        log.debug("Request to save Reponse : {}", reponse);
        // no save call needed as we have no fields that can be updated
        return reponse;
    }

    @Override
    public Optional<Reponse> partialUpdate(Reponse reponse) {
        log.debug("Request to partially update Reponse : {}", reponse);

        return reponseRepository
            .findById(reponse.getId())
            .map(existingReponse -> {
                return existingReponse;
            }); // .map(reponseRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Reponse> findAll(Pageable pageable) {
        log.debug("Request to get all Reponses");
        return reponseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reponse> findOne(Long id) {
        log.debug("Request to get Reponse : {}", id);
        return reponseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reponse : {}", id);
        reponseRepository.deleteById(id);
    }
}
