package io.feedback.survey.web.service;

import io.feedback.survey.entity.Page;
import io.feedback.survey.entity.Result;
import io.feedback.survey.repository.ResultRepository;
import io.feedback.survey.web.dto.PageFormDto;
import io.feedback.survey.web.dto.ParticipationDto;
import io.feedback.survey.web.model.PageModel;
import io.feedback.survey.web.model.PageModelMockProvider;
import io.feedback.survey.web.validator.PageModelValidator;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
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

    private ResultService resultService;

    @Before
    public void setUp() {
        resultService = new ResultService();
        resultService.setPageModelValidator(mock(PageModelValidator.class));
        resultService.setResultRepository(mock(ResultRepository.class));
    }

    @Test
    public void setPageModelValidator_SomePageModelValidator_SameValueIsReturnedByGetPageModelValidator() {
        PageModelValidator pageModelValidatorMock = mock(PageModelValidator.class);

        resultService.setPageModelValidator(pageModelValidatorMock);

        assertEquals(pageModelValidatorMock, resultService.getPageModelValidator());
    }

    @Test
    public void setResultRepository_SomeResultRepository_SameValueIsReturnedByGetResultRepository() {
        ResultRepository resultRepositoryMock = mock(ResultRepository.class);

        resultService.setResultRepository(resultRepositoryMock);

        assertEquals(resultRepositoryMock, resultService.getResultRepository());
    }

    @Test
    @Parameters(method = "parametersForSaveResultsIfValid")
    public void saveResultsIfValid_SomeArguments_ValidateMethodOfPageModelValidatorIsCalled(
            PageFormDto pageFormDtoMock,
            ParticipationDto participationDtoMock) {
        PageModel pageModelMock = pageFormDtoMock.getPageModel();
        BindingResult bindingResultMock = pageFormDtoMock.getBindingResult();
        Page pageMock = pageFormDtoMock.getPage();

        resultService.saveResultsIfValid(pageFormDtoMock, participationDtoMock);

        verify(resultService.getPageModelValidator()).validate(pageModelMock, bindingResultMock, pageMock);
    }

    @Test
    @Parameters(method = "parametersForSaveResultsIfValid")
    public void saveResultsIfValid_NoErrorsExist_ResultsAreSaved(PageFormDto pageFormDtoMock,
                                                                 ParticipationDto participationDtoMock) {
        BindingResult bindingResultMock = pageFormDtoMock.getBindingResult();
        when(bindingResultMock.hasErrors()).thenReturn(false);

        boolean saved = resultService.saveResultsIfValid(pageFormDtoMock, participationDtoMock);

        assertEquals(true, saved);
    }

    @Test
    @Parameters(method = "parametersForSaveResultsIfValid")
    public void saveResultsIfValid_ErrorsExist_ResultsAreNotSaved(PageFormDto pageFormDtoMock,
                                                                  ParticipationDto participationDtoMock) {
        BindingResult bindingResultMock = pageFormDtoMock.getBindingResult();
        when(bindingResultMock.hasErrors()).thenReturn(true);

        assertEquals(false, resultService.saveResultsIfValid(pageFormDtoMock, participationDtoMock));
    }

    @Test
    public void saveResultsWithParticipationData_SomeResults_SaveResultsMethodOfResultRepositoryIsCalled() {
        Result resultMock = mock(Result.class);
        List<Result> resultMocks = new ArrayList<>();
        resultMocks.add(resultMock);

        resultService.saveResultsWithParticipationData(resultMocks, mock(ParticipationDto.class));

        verify(resultService.getResultRepository()).saveResults(resultMocks);
    }

    @Test
    public void saveResultsWithParticipationData_SomeResultsAndSomeParticipationIdentifier_ParticipationIdentifierIsSetForResults() {
        String participationIdentifier = "Participation identifier";
        ParticipationDto participationDtoMock = mock(ParticipationDto.class);
        when(participationDtoMock.getIdentifier()).thenReturn(participationIdentifier);
        Result resultMock = mock(Result.class);
        List<Result> resultMocks = new ArrayList<>();
        resultMocks.add(resultMock);

        resultService.saveResultsWithParticipationData(resultMocks, participationDtoMock);

        verify(resultMock).setParticipationIdentifier(participationIdentifier);
    }

    @Test
    public void saveResultsWithParticipationData_SomeResultsAndSomeRemoteAddress_RemoteAddressIsSetForResults() {
        String remoteAddress = "127.0.0.1";
        ParticipationDto participationDtoMock = mock(ParticipationDto.class);
        when(participationDtoMock.getRemoteAddress()).thenReturn(remoteAddress);
        Result resultMock = mock(Result.class);
        List<Result> resultMocks = new ArrayList<>();
        resultMocks.add(resultMock);

        resultService.saveResultsWithParticipationData(resultMocks, participationDtoMock);

        verify(resultMock).setRemoteAddress(remoteAddress);
    }

    @Test
    @Parameters(source = PageModelMockProvider.class, method = "provideOneWithCountOfResults")
    public void extractResultsFromPageModel_PageModelWithResults_CorrectCountOfResultsIsExtracted(
            PageModel pageModelMock,
            int countOfResults) {
        List<Result> results = resultService.extractResultsFromPageModel(pageModelMock);

        assertEquals(countOfResults, results.size());
    }

    @Test
    public void extractResultsFromPageModel_PageModelWithoutAnyQuestionModel_EmptyListIsExtracted() {
        PageModel pageModelMock = mock(PageModel.class);
        when(pageModelMock.getQuestionModels()).thenReturn(null);

        List<Result> results = resultService.extractResultsFromPageModel(pageModelMock);

        assertEquals(0, results.size());
    }

    private Object[] parametersForSaveResultsIfValid() {
        PageFormDto pageFormDtoMock = mock(PageFormDto.class);
        when(pageFormDtoMock.getPageModel()).thenReturn(mock(PageModel.class));
        when(pageFormDtoMock.getBindingResult()).thenReturn(mock(BindingResult.class));
        when(pageFormDtoMock.getPage()).thenReturn(mock(Page.class));
        return new Object[]{
                new Object[]{pageFormDtoMock, mock(ParticipationDto.class)},
        };
    }
}