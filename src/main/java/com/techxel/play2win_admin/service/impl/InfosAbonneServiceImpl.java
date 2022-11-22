package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.InfosAbonne;
import com.techxel.play2win_admin.repository.InfosAbonneRepository;
import com.techxel.play2win_admin.service.InfosAbonneService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link InfosAbonne}.
 */
@Service
@Transactional
public class InfosAbonneServiceImpl implements InfosAbonneService {

    private final Logger log = LoggerFactory.getLogger(InfosAbonneServiceImpl.class);

    private final InfosAbonneRepository infosAbonneRepository;

    public InfosAbonneServiceImpl(InfosAbonneRepository infosAbonneRepository) {
        this.infosAbonneRepository = infosAbonneRepository;
    }

    @Override
    public InfosAbonne save(InfosAbonne infosAbonne) {
        log.debug("Request to save InfosAbonne : {}", infosAbonne);
        return infosAbonneRepository.save(infosAbonne);
    }

    @Override
    public InfosAbonne update(InfosAbonne infosAbonne) {
        log.debug("Request to save InfosAbonne : {}", infosAbonne);
        // no save call needed as we have no fields that can be updated
        return infosAbonne;
    }

    @Override
    public Optional<InfosAbonne> partialUpdate(InfosAbonne infosAbonne) {
        log.debug("Request to partially update InfosAbonne : {}", infosAbonne);

        return infosAbonneRepository
            .findById(infosAbonne.getId())
            .map(existingInfosAbonne -> {
                return existingInfosAbonne;
            }); // .map(infosAbonneRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InfosAbonne> findAll(Pageable pageable) {
        log.debug("Request to get all InfosAbonnes");
        return infosAbonneRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InfosAbonne> findOne(Long id) {
        log.debug("Request to get InfosAbonne : {}", id);
        return infosAbonneRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InfosAbonne : {}", id);
        infosAbonneRepository.deleteById(id);
    }
}
