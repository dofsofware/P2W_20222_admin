package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.Play;
import com.techxel.play2win_admin.repository.PlayRepository;
import com.techxel.play2win_admin.service.PlayService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Play}.
 */
@Service
@Transactional
public class PlayServiceImpl implements PlayService {

    private final Logger log = LoggerFactory.getLogger(PlayServiceImpl.class);

    private final PlayRepository playRepository;

    public PlayServiceImpl(PlayRepository playRepository) {
        this.playRepository = playRepository;
    }

    @Override
    public Play save(Play play) {
        log.debug("Request to save Play : {}", play);
        return playRepository.save(play);
    }

    @Override
    public Play update(Play play) {
        log.debug("Request to save Play : {}", play);
        // no save call needed as we have no fields that can be updated
        return play;
    }

    @Override
    public Optional<Play> partialUpdate(Play play) {
        log.debug("Request to partially update Play : {}", play);

        return playRepository
            .findById(play.getId())
            .map(existingPlay -> {
                return existingPlay;
            }); // .map(playRepository::save)
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Play> findAll(Pageable pageable) {
        log.debug("Request to get all Plays");
        return playRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Play> findOne(Long id) {
        log.debug("Request to get Play : {}", id);
        return playRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Play : {}", id);
        playRepository.deleteById(id);
    }
}
