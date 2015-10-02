package io.feedback.survey.web;

import io.feedback.survey.entity.Page;
import io.feedback.survey.service.SurveyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SurveyController {

    private SurveyService surveyService;

    public SurveyService getSurveyService() {
        return surveyService;
    }

    @Autowired
    public void setSurveyService(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @RequestMapping(value = "/survey/{surveyId}/{pageNumber}")
    public String page(@PathVariable Long surveyId,
            @PathVariable Integer pageNumber,
            @ModelAttribute("surveyForm") SurveyForm surveyForm, Model model, BindingResult result) {
        System.out.println(result.hasErrors());
        Page page = getSurveyService().loadSurveyPage(surveyId, pageNumber);
        model.addAttribute("page", page);
        return "survey/page";
    }
}