package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.Resultat;
import com.techxel.play2win_admin.repository.ResultatRepository;
import com.techxel.play2win_admin.service.ResultatService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Resultat}.
 */
@Service
@Transactional
public class ResultatServiceImpl implements ResultatService {

    private final Logger log = LoggerFactory.getLogger(ResultatServiceImpl.class);

    private final ResultatRepository resultatRepository;

    public ResultatServiceImpl(ResultatRepository resultatRepository) {
        this.resultatRepository = resultatRepository;
    }

    @Override
    public Resultat save(Resultat resultat) {
        log.debug("Request to save Resultat : {}", resultat);
        return resultatRepository.save(resultat);
    }

    @Override
    public Resultat update(Resultat resultat) {
        log.debug("Request to save Resultat : {}", resultat);
        // no save call needed as we have no fields that can be updated
        return resultat;
    }

    @Override
    public Optional<Resultat> partialUpdate(Resultat resultat) {
        log.debug("Request to partially update Resultat : {}", resultat);

        return resultatRepository
            .findById(resultat.getId())
            .map(existingResultat -> {
                return existingResultat;
            }); // .map(resultatRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Resultat> findAll(Pageable pageable) {
        log.debug("Request to get all Resultats");
        return resultatRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Resultat> findOne(Long id) {
        log.debug("Request to get Resultat : {}", id);
        return resultatRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Resultat : {}", id);
        resultatRepository.deleteById(id);
    }
}
