package at.IDEE;

public class ExtraContent
{
    static QuizQuestions getQuizQestions()
    {
        System.out.println("getQuizQuestions");
        QuizQuestions quiz = new QuizQuestions();

        QuizQuestion q1 = new QuizQuestion();
        {
            q1.questionText = "What is my Name?";
            q1.difficulty = "einfach";
            q1.explanation = "Schau mehr Breaking Bad!";
            AnswerOption a1 = new AnswerOption();
            a1.text = "Heisenberg";
            a1.isCorrect = true;
            AnswerOption a2 = new AnswerOption();
            a2.text = "Einstein";
            a2.isCorrect = false;
            AnswerOption a3 = new AnswerOption();
            a3.text = "Faraday";
            a3.isCorrect = false;
            AnswerOption a4 = new AnswerOption();
            a4.text = "Gauß";
            a4.isCorrect = false;
            q1.options.add(a1);
            q1.options.add(a2);
            q1.options.add(a3);
            q1.options.add(a4);
        }
        QuizQuestion q2 = new QuizQuestion();
        {
            q2.questionText = "Tick, Trick und ";
            q2.difficulty = "einfach";
            q2.explanation = "Lies mehr Micky Maus";
            AnswerOption a1 = new AnswerOption();
            a1.text = "Treck";
            a1.isCorrect = false;
            AnswerOption a2 = new AnswerOption();
            a2.text = "Truck";
            a2.isCorrect = false;
            AnswerOption a3 = new AnswerOption();
            a3.text = "Track";
            a3.isCorrect = true;
            AnswerOption a4 = new AnswerOption();
            a4.text = "Trock";
            a4.isCorrect = false;
            q2.options.add(a1);
            q2.options.add(a2);
            q2.options.add(a3);
            q2.options.add(a4);
        }
        QuizQuestion q3 = new QuizQuestion();
        {
            q3.questionText = "Der beste Superhero";
            q3.difficulty = "einfach";
            q3.explanation = "Get Gud";
            AnswerOption a1 = new AnswerOption();
            a1.text = "Batman";
            a1.isCorrect = false;
            AnswerOption a2 = new AnswerOption();
            a2.text = "Superman";
            a2.isCorrect = false;
            AnswerOption a3 = new AnswerOption();
            a3.text = "Captain America";
            a3.isCorrect = false;
            AnswerOption a4 = new AnswerOption();
            a4.text = "Iron Man";
            a4.isCorrect = true;
            q3.options.add(a1);
            q3.options.add(a2);
            q3.options.add(a3);
            q3.options.add(a4);
        }

        quiz.questions.add(q1);
        quiz.questions.add(q2);
        quiz.questions.add(q3);

        return quiz;
    }

    static Funfact getFunfact()
    {
        System.out.println("getFunfact");
        Funfact funfact = new Funfact();
        funfact.text = "Wer mag schon lustige Fakten über gesetzte?";
        return funfact;
    }
}
