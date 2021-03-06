package io.feedback.survey.web.validator;

import io.feedback.survey.entity.Question;
import io.feedback.survey.entity.Result;
import io.feedback.survey.entity.ResultMockProvider;
import io.feedback.survey.web.model.QuestionModel;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.Errors;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(locations = {"/test-spring-config.xml"})
public class QuestionModelValidatorTest {

    private QuestionModelValidator questionModelValidator;

    private QuestionModel questionModelMock;

    private Errors errorsMock;

    private Question questionMock;

    @Before
    public void setUp() {
        questionModelValidator = new QuestionModelValidator();
        questionModelValidator.setResultValidator(mock(ResultValidator.class));

        questionModelMock = mock(QuestionModel.class);
        errorsMock = mock(Errors.class);
        questionMock = mock(Question.class);
    }

    @Test
    public void setResultValidator_SomeResultValidator_SameValueIsReturnedByGetResultValidator() {
        ResultValidator resultValidatorMock = mock(ResultValidator.class);

        questionModelValidator.setResultValidator(resultValidatorMock);

        assertEquals(resultValidatorMock, questionModelValidator.getResultValidator());
    }

    @Test
    @Parameters(source = ResultMockProvider.class, method = "provideListWithoutId")
    public void validate_InvalidResults_InvalidAnswerErrorIsSetForAnyInvalidResult(List<Result> resultMocks) {
        when(questionModelMock.getResults()).thenReturn(resultMocks);
        for (Result resultMock : resultMocks) {
            when(questionModelValidator.getResultValidator().isValid(resultMock)).thenReturn(false);
        }

        questionModelValidator.validate(questionModelMock, errorsMock, questionMock);

        for (int i = 0; i < resultMocks.size(); i++) {
            verify(errorsMock).rejectValue("questionModels[" + questionMock.getId() + "].results[" + i + "]",
                    "error.answer_is_invalid");
        }
    }

    @Test
    @Parameters(source = ResultMockProvider.class, method = "provideListWithoutAnswer")
    public void validate_ValidResults_NotAnsweredErrorIsSetForQuestion(List<Result> resultMocks) {
        when(questionModelMock.getResults()).thenReturn(resultMocks);
        for (Result resultMock : resultMocks) {
            when(questionModelValidator.getResultValidator().isValid(resultMock)).thenReturn(true);
        }

        questionModelValidator.validate(questionModelMock, errorsMock, questionMock);

        verify(errorsMock).rejectValue("questionModels[" + questionMock.getId() + "]", "error.question_not_answered");
    }

    @Test
    @Parameters(source = ResultMockProvider.class, method = "provideListWithoutId")
    public void validate_ValidResults_NotAnsweredErrorIsNeverSet(List<Result> resultMocks) {
        when(questionModelMock.getResults()).thenReturn(resultMocks);
        for (Result resultMock : resultMocks) {
            when(questionModelValidator.getResultValidator().isValid(resultMock)).thenReturn(true);
        }

        questionModelValidator.validate(questionModelMock, errorsMock, questionMock);

        verify(errorsMock, never())
                .rejectValue("questionModels[" + questionMock.getId() + "]", "", "No answer selected");
    }

    @Test
    @Parameters(source = ResultMockProvider.class, method = "provideListWithoutId")
    public void validate_ValidResults_InvalidAnswerErrorIsNeverSet(List<Result> resultMocks) {
        when(questionModelMock.getResults()).thenReturn(resultMocks);
        for (Result resultMock : resultMocks) {
            when(questionModelValidator.getResultValidator().isValid(resultMock)).thenReturn(true);
        }

        questionModelValidator.validate(questionModelMock, errorsMock, questionMock);

        for (int i = 0; i < resultMocks.size(); i++) {
            verify(errorsMock, never())
                    .rejectValue("questionModels[" + questionMock.getId() + "].results[" + i + "]", "",
                            "Invalid answer");
        }
    }
}