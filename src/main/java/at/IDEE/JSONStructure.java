package at.IDEE;

import java.time.LocalDate;
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

class AskLawDetail
{
    String id;
    String datetime;
    String address;
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
    String lawyer;
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
    int week;
    List<QuizQuestion> questions = new ArrayList<>();
}

class Quizes
{
    List<QuizQuestions> quiz = new ArrayList<>();
}