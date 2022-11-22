package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.Gains;
import com.techxel.play2win_admin.repository.GainsRepository;
import com.techxel.play2win_admin.service.GainsService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Gains}.
 */
@Service
@Transactional
public class GainsServiceImpl implements GainsService {

    private final Logger log = LoggerFactory.getLogger(GainsServiceImpl.class);

    private final GainsRepository gainsRepository;

    public GainsServiceImpl(GainsRepository gainsRepository) {
        this.gainsRepository = gainsRepository;
    }

    @Override
    public Gains save(Gains gains) {
        log.debug("Request to save Gains : {}", gains);
        return gainsRepository.save(gains);
    }

    @Override
    public Gains update(Gains gains) {
        log.debug("Request to save Gains : {}", gains);
        return gainsRepository.save(gains);
    }

    @Override
    public Optional<Gains> partialUpdate(Gains gains) {
        log.debug("Request to partially update Gains : {}", gains);

        return gainsRepository
            .findById(gains.getId())
            .map(existingGains -> {
                if (gains.getTelephone() != null) {
                    existingGains.setTelephone(gains.getTelephone());
                }
                if (gains.getMinute() != null) {
                    existingGains.setMinute(gains.getMinute());
                }
                if (gains.getMegabit() != null) {
                    existingGains.setMegabit(gains.getMegabit());
                }
                if (gains.getCreatedAt() != null) {
                    existingGains.setCreatedAt(gains.getCreatedAt());
                }

                return existingGains;
            })
            .map(gainsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Gains> findAll(Pageable pageable) {
        log.debug("Request to get all Gains");
        return gainsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Gains> findOne(Long id) {
        log.debug("Request to get Gains : {}", id);
        return gainsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gains : {}", id);
        gainsRepository.deleteById(id);
    }
}
