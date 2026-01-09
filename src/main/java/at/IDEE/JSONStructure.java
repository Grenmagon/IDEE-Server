package at.IDEE;

import java.util.ArrayList;
import java.util.List;

class OllamaPrompt
{
    String model;
    String prompt;
    boolean stream = true;
}

class OllamaResponse
{
    String model;
    String created_at;
    String response;
    boolean done;
}

class CategoriesAnswer
{
    List<String> categories = new ArrayList<>();
}

class Funfact
{
    String text;
}

class LawDetailShort
{
    String url;
    String dokid;
    String id;
    String title;
}

class LawDetailsShort
{
    public List<LawDetailShort> lawDetailShort = new ArrayList<>();
}

class LawDetail
{
    String id;
    String title;
    String category;
    String paragraph;
    String summary;
    String officialText;
    String source;
}

class AnswerOption
{
    String text;
    boolean isCorrect;
}

class QuizQuestion
{
    String questionText;
    String difficulty;
    List<AnswerOption> options = new ArrayList<>();
    String explanation;
}

class QuizQuestions
{
    List<QuizQuestion> questions = new ArrayList<>();
}
