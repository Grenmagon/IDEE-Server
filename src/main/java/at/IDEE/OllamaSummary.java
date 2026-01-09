package at.IDEE;

public class OllamaSummary {
    public static void main(String[] args) {
        try
        {
            String id = "BGBLA_2017_I_95";  // durch auswahl zu ersetzen
            summarizeBgbl(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static void summarizeBgbl(String id) throws Exception {
        OllamaClient oc = new OllamaClient();
        String model = "gemma3";
        String BaseURL = "https://www.ris.bka.gv.at/Dokumente/BgblAuth/";
        String URL = BaseURL + id + "/" + id + ".rtf";
        String prePrompt = "Fasse mir diese österreichische Gesetz kurz und einfach zusammen und beziehe dich vor allem auf Punkte, die sich auf die Rechte bzw. Pflichten von Privatpersonen beziehen. Antworte ausschließlich mit der Zusammenfassung selbst. Keine Einleitung, keine Wiederholung der Aufgabe, keine Links, keine Disclaimer. Die Zusammenfassung darf ansprechend gestaltet sein.";

        String prompt = prePrompt + URL;
        System.out.println(oc.ask(model, prompt));
    }
}
