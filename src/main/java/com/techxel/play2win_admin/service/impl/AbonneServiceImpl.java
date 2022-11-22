package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.Abonne;
import com.techxel.play2win_admin.repository.AbonneRepository;
import com.techxel.play2win_admin.service.AbonneService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Abonne}.
 */
@Service
@Transactional
public class AbonneServiceImpl implements AbonneService {

    private final Logger log = LoggerFactory.getLogger(AbonneServiceImpl.class);

    private final AbonneRepository abonneRepository;

    public AbonneServiceImpl(AbonneRepository abonneRepository) {
        this.abonneRepository = abonneRepository;
    }

    @Override
    public Abonne save(Abonne abonne) {
        log.debug("Request to save Abonne : {}", abonne);
        return abonneRepository.save(abonne);
    }

    @Override
    public Abonne update(Abonne abonne) {
        log.debug("Request to save Abonne : {}", abonne);
        return abonneRepository.save(abonne);
    }

    @Override
    public Optional<Abonne> partialUpdate(Abonne abonne) {
        log.debug("Request to partially update Abonne : {}", abonne);

        return abonneRepository
            .findById(abonne.getId())
            .map(existingAbonne -> {
                if (abonne.getIdentifiant() != null) {
                    existingAbonne.setIdentifiant(abonne.getIdentifiant());
                }
                if (abonne.getTelephone() != null) {
                    existingAbonne.setTelephone(abonne.getTelephone());
                }
                if (abonne.getMotDePasse() != null) {
                    existingAbonne.setMotDePasse(abonne.getMotDePasse());
                }
                if (abonne.getScore() != null) {
                    existingAbonne.setScore(abonne.getScore());
                }
                if (abonne.getNiveau() != null) {
                    existingAbonne.setNiveau(abonne.getNiveau());
                }
                if (abonne.getCreatedAt() != null) {
                    existingAbonne.setCreatedAt(abonne.getCreatedAt());
                }
                if (abonne.getDernierePaticipation() != null) {
                    existingAbonne.setDernierePaticipation(abonne.getDernierePaticipation());
                }
                if (abonne.getActif() != null) {
                    existingAbonne.setActif(abonne.getActif());
                }
                if (abonne.getCodeRactivation() != null) {
                    existingAbonne.setCodeRactivation(abonne.getCodeRactivation());
                }

                return existingAbonne;
            })
            .map(abonneRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Abonne> findAll(Pageable pageable) {
        log.debug("Request to get all Abonnes");
        return abonneRepository.findAll(pageable);
    }

    public Page<Abonne> findAllWithEagerRelationships(Pageable pageable) {
        return abonneRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Abonne> findOne(Long id) {
        log.debug("Request to get Abonne : {}", id);
        return abonneRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Abonne : {}", id);
        abonneRepository.deleteById(id);
    }
}
