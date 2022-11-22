package com.techxel.play2win_admin.service.impl;

import com.techxel.play2win_admin.domain.Question;
import com.techxel.play2win_admin.repository.QuestionRepository;
import com.techxel.play2win_admin.service.QuestionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Question}.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question save(Question question) {
        log.debug("Request to save Question : {}", question);
        return questionRepository.save(question);
    }

    @Override
    public Question update(Question question) {
        log.debug("Request to save Question : {}", question);
        return questionRepository.save(question);
    }

    @Override
    public Optional<Question> partialUpdate(Question question) {
        log.debug("Request to partially update Question : {}", question);

        return questionRepository
            .findById(question.getId())
            .map(existingQuestion -> {
                if (question.getQuiz() != null) {
                    existingQuestion.setQuiz(question.getQuiz());
                }
                if (question.getReponse1() != null) {
                    existingQuestion.setReponse1(question.getReponse1());
                }
                if (question.getReponse2() != null) {
                    existingQuestion.setReponse2(question.getReponse2());
                }
                if (question.getReponse3() != null) {
                    existingQuestion.setReponse3(question.getReponse3());
                }
                if (question.getReponse4() != null) {
                    existingQuestion.setReponse4(question.getReponse4());
                }
                if (question.getBonneReponse() != null) {
                    existingQuestion.setBonneReponse(question.getBonneReponse());
                }

                return existingQuestion;
            })
            .map(questionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Question> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Question> findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        return questionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.deleteById(id);
    }
}
