package io.feedback.survey.web.service;

import io.feedback.survey.entity.Page;
import io.feedback.survey.entity.Result;
import io.feedback.survey.repository.ResultRepository;
import io.feedback.survey.web.model.PageModel;
import io.feedback.survey.web.model.PageModelProvider;
import io.feedback.survey.web.validator.PageModelValidator;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(locations = {"/test-spring-config.xml"})
public class ResultServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ResultService resultService;

    @Before
    public void setUp() {
        resultService = new ResultService();
        resultService.setPageModelValidator(mock(PageModelValidator.class));
        resultService.setResultRepository(mock(ResultRepository.class));
    }

    @Test
    public void getAndSetPageModelValidator() {
        PageModelValidator pageModelValidatorMock = mock(PageModelValidator.class);
        resultService.setPageModelValidator(pageModelValidatorMock);
        assertEquals(pageModelValidatorMock, resultService.getPageModelValidator());
    }

    @Test
    public void getAndSetResultRepository() {
        ResultRepository resultRepositoryMock = mock(ResultRepository.class);
        resultService.setResultRepository(resultRepositoryMock);
        assertEquals(resultRepositoryMock, resultService.getResultRepository());
    }

    @Test
    @Parameters(method = "parametersForSaveResultsIfValid")
    public void saveResultsIfValidValidates(PageModel pageModelMock, BindingResult bindingResultMock, Page pageMock) {
        when(bindingResultMock.hasErrors()).thenReturn(false);
        resultService.saveResultsIfValid(pageModelMock, bindingResultMock, pageMock);
        verify(resultService.getPageModelValidator()).validate(pageModelMock, bindingResultMock, pageMock);
    }

    @Test
    @Parameters(method = "parametersForSaveResultsIfValid")
    public void saveResultsIfValidSavesWithoutErrors(PageModel pageModelMock, BindingResult bindingResultMock,
                                                     Page pageMock) {
        when(bindingResultMock.hasErrors()).thenReturn(false);
        assertEquals(true, resultService.saveResultsIfValid(pageModelMock, bindingResultMock, pageMock));
    }

    @Test
    @Parameters(method = "parametersForSaveResultsIfValid")
    public void saveResultsIfValidSavesNotWithErrors(PageModel pageModelMock, BindingResult bindingResultMock,
                                                     Page pageMock) {
        when(bindingResultMock.hasErrors()).thenReturn(true);
        assertEquals(false, resultService.saveResultsIfValid(pageModelMock, bindingResultMock, pageMock));
    }

    private Object[] parametersForSaveResultsIfValid() {
        return new Object[]{
                new Object[]{mock(PageModel.class), mock(BindingResult.class), mock(Page.class)},
        };
    }

    @Test
    public void saveResultsCallsSaveMethodInRepository() {
        Result resultMock = mock(Result.class);
        List<Result> resultMocks = new ArrayList<>();
        resultMocks.add(resultMock);
        resultService.saveResults(resultMocks);
        verify(resultService.getResultRepository()).saveResults(resultMocks);
    }

    @Test
    @Parameters(source = PageModelProvider.class, method = "provideForCountResults")
    public void extractResultsFromPageModelExtractsCorrectCount(PageModel pageModelMock, int countOfResults) {
        List<Result> results = resultService.extractResultsFromPageModel(pageModelMock);
        assertEquals(countOfResults, results.size());
    }

    @Test
    public void extractResultsFromPageModelExtractsEmptyListWhenNoQuestionIsAnswered() {
        PageModel pageModelMock = mock(PageModel.class);
        when(pageModelMock.getQuestionModels()).thenReturn(null);
        List<Result> results = resultService.extractResultsFromPageModel(pageModelMock);
        assertEquals(0, results.size());
    }
}