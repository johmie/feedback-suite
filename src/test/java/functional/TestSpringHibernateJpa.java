package functional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.feedback.survey.entity.Answer;
import io.feedback.survey.entity.Page;
import io.feedback.survey.entity.Question;
import io.feedback.survey.entity.Survey;
import io.feedback.survey.service.AnswerService;
import io.feedback.survey.service.PageService;
import io.feedback.survey.service.QuestionService;
import io.feedback.survey.service.SurveyService;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TestSpringHibernateJpa {

    private SurveyService surveyService;

    private PageService pageService;

    private QuestionService questionService;

    private AnswerService answerService;

    @PersistenceContext
    private EntityManager entityManager;

    public void test(ApplicationContext context) {

        entityManager.createNativeQuery("DELETE FROM Survey").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE Survey AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE Page AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE Question AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE Answer AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE Result AUTO_INCREMENT = 1").executeUpdate();

        surveyService = (SurveyService) context.getBean("surveyService");
        pageService = (PageService) context.getBean("pageService");
        questionService = (QuestionService) context.getBean("questionService");
        answerService = (AnswerService) context.getBean("answerService");

        Survey survey = createSurvey();
        createPages(survey);
        System.out.println("End of test");
        //listPages();
    }

    public Survey createSurvey() {

        Survey survey = new Survey();
        survey.setName("Internal name of my first project");
        survey.setTitle("Title of my first project");

        surveyService.saveSurvey(survey);

        System.out.println("Survey: " + survey + " added successfully");

        return survey;
    }

    public void createPages(Survey survey) {

        Page page = new Page();
        page.setName("Page #1");
        page.setTitle("Page #1");
        page.setSurvey(survey);
        pageService.savePage(page);
        System.out.println("Page #1: " + page + " added successfully");

//        Page page2 = new Page();
//        page2.setName("Page #2");
//        page2.setTitle("Page #2");
//        page2.setSurvey(survey);
//        pageService.addPage(page2);
//        System.out.println("Page #2: " + page2 + " added successfully");
//        
//        Page page3 = new Page();
//        page3.setName("Page #3");
//        page3.setTitle("Page #3");
//        page3.setSurvey(survey);
//        pageService.addPage(page3);
//        System.out.println("Page #3: " + page3 + " added successfully");

        createQuestions(page);
        System.out.println("Questions added to Page #1");
    }

    private void createQuestions(Page page) {

        Question question1 = new Question();
        question1.setType(io.feedback.survey.entity.Question.Type.SINGLE_CHOICE);
        question1.setName("Schönste Farbe");
        question1.setTitle("Einfachauswahl: Welche Farbe ist die schönste?");
        question1.setPosition(1);
        question1.setPage(page);
        questionService.saveQuestion(question1);
        createAnswers1(question1);

        Question question2 = new Question();
        question2.setType(io.feedback.survey.entity.Question.Type.MULTIPLE_CHOICE);
        question2.setName("Farben");
        question2.setTitle("Mehrfachauswahl: Welche Farben findest du schön?");
        question2.setPosition(2);
        question2.setPage(page);
        questionService.saveQuestion(question2);
        createAnswers1(question2);

        Question question3 = new Question();
        question3.setType(io.feedback.survey.entity.Question.Type.MATRIX);
        question3.setName("Farben Matrix");
        question3.setTitle("Matrix: Welche Farben findest du schön?");
        question3.setPosition(4);
        question3.setPage(page);
        questionService.saveQuestion(question3);
        createAnswers1(question3);

        Question question4 = new Question();
        question4.setType(io.feedback.survey.entity.Question.Type.MULTIPLE_CHOICE);
        question4.setName("Farben 2");
        question4.setTitle("Mehrfachauswahl 2: Welche Farben findest du schön?");
        question4.setPosition(3);
        question4.setPage(page);
        questionService.saveQuestion(question4);
        createAnswers1(question4);
    }

    private void createAnswers1(Question question) {

        Answer answer1 = new Answer();
        answer1.setName("Antwortoption #1");
        answer1.setTitle("grün");
        answer1.setValue("#00ff00");
        answer1.setPosition(1);
        answer1.setQuestion(question);
        answerService.saveAnswer(answer1);

        Answer answer2 = new Answer();
        answer2.setName("Antwortoption #2");
        answer2.setTitle("rot");
        answer2.setValue("#ff0000");
        answer2.setPosition(2);
        answer2.setQuestion(question);
        answerService.saveAnswer(answer2);

        Answer answer3 = new Answer();
        answer3.setName("Antwortoption #3");
        answer3.setTitle("blau");
        answer3.setValue("#0000ff");
        answer3.setPosition(3);
        answer3.setQuestion(question);
        answerService.saveAnswer(answer3);

        Answer answer4 = new Answer();
        answer4.setName("Antwortoption #4");
        answer4.setTitle("freitext");
        answer4.setValueType(io.feedback.survey.entity.Answer.ValueType.FREE_TEXT);
        answer4.setPosition(4);
        answer4.setQuestion(question);
        answerService.saveAnswer(answer4);
    }
}