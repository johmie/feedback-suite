package io.feedback.survey.repository;

import io.feedback.core.repository.AbstractBaseRepository;
import io.feedback.survey.entity.Answer;

import org.springframework.stereotype.Repository;

@Repository
public class AnswerRepository extends AbstractBaseRepository<Answer> {

    @Override
    public void insertOrUpdate(Answer answer) {
        getEntityManager().persist(answer);
    }

    @Override
    public Answer fetchById(Long id) {
        return getEntityManager().find(Answer.class, id);
    }
}