package at.IDEE;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.WeekFields;


public class ExtraContent
{

    private static final Gson GSON = new Gson();
    static private  LocalDate funfactDate = null;
    static private  Funfact funfact = null;
    static QuizQuestions getQuizQestions()
    {
        System.out.println("getQuizQestions");
        InputStream is = ExtraContent.class
                .getClassLoader()
                .getResourceAsStream("Quiz.json");

        if (is == null) {
            throw new RuntimeException("Quiz.json nicht gefunden");
        }
        System.out.println("All Quizes geladen");
        Quizes quizes = GSON.fromJson(new InputStreamReader(is), Quizes.class);
        System.out.println("from json convertet");
        int kw = LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear());
        for (QuizQuestions quiz: quizes.quiz)
        {
            System.out.println(quiz.week +":"+ kw);
            if (quiz.week == kw)
                return quiz;
        }

        System.out.println("Quiz konnte nicht geladen werden!");
        return new QuizQuestions();
    }

    static Funfact getFunfact()
    {
        if (funfactDate == null)
        {
            funfactDate = LocalDate.now();
            createFunfact();
        }

        if (!LocalDate.now().equals(funfactDate))
            createFunfact();

        return funfact;
    }

    static private void createFunfact()
    {
        System.out.println("createFunfact!");
        funfact = new Funfact();
        try
        {
            funfact.text = OllamaClient.getOllamaClient().getFunfact();
        }
        catch (Exception e)
        {
            funfact.text = "Böse Goblins haben die Funfacts gestohlen!!";
        }
        System.out.println(funfact.text);
    }
}
