package at.IDEE;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class OllamaClient
{
    public class OllamaMessage
    {
        public String role;     // system | user | assistant
        public String content;

        public OllamaMessage(String role, String content)
        {
            this.role = role;
            this.content = content;
        }
    }

    public class OllamaChatRequest
    {
        public String model;
        public List<OllamaMessage> messages;
        public boolean stream = true;
    }

    public class OllamaChatResponse
    {
        public OllamaMessage message;
        public boolean done;
    }

    private final HttpClient httpClient;
    private final String baseUrl;
    private final String API = "/api/";
    ;
    private final String GENERATE = API + "generate";
    private final String CHAT = API + "chat";
    private final String MODEL = "llama3.2";


    private Gson gson = new Gson();

    private static OllamaClient singleton = null;

    private OllamaClient()
    {
        this("http://localhost:11434");
    }

    private OllamaClient(String baseUrl)
    {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
    }

    public static OllamaClient getOllamaClient()
    {
        if (singleton == null)
            singleton = new OllamaClient();

        return singleton;
    }


    public String ask(String prompt) throws Exception
    {
        return ask(MODEL, prompt);
    }

    public String ask(String model, String prompt) throws Exception
    {
        OllamaPrompt op = new OllamaPrompt();
        op.model = model;
        op.prompt = prompt;
        String json = gson.toJson(op);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + GENERATE))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null)
        {
            OllamaResponse resp = gson.fromJson(line, OllamaResponse.class);
            sb.append(resp.response);
        }

        return sb.toString();
    }

    public String chat(List<OllamaMessage> conversation) throws Exception
    {
        return chat(MODEL, conversation);
    }

    public String chat(String model, List<OllamaMessage> conversation) throws Exception
    {

        OllamaChatRequest req = new OllamaChatRequest();
        req.model = model;
        req.messages = conversation;

        String json = gson.toJson(req);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + CHAT))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<InputStream> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()));
        StringBuilder answer = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null)
        {
            OllamaChatResponse resp = gson.fromJson(line, OllamaChatResponse.class);

            if (resp.message != null && resp.message.content != null)
            {
                answer.append(resp.message.content);
            }

            if (resp.done) break;
        }

        return answer.toString();
    }

    private List<OllamaMessage> fork(List<OllamaMessage> base)
    {
        return new ArrayList<>(base); // shallow copy reicht
    }

    void summarizeBgBl(LawDetail ld, String xml, String time) throws Exception
    {
        List<OllamaMessage> conversation = new ArrayList<>();

        conversation.add(new OllamaMessage(
                "system",
                """
                Antworte ausschließlich auf Deutsch.
                Du erklärst österreichische Gesetze in einfacher, verständlicher Sprache.
                Du gibst keine Rechtsberatung.
            
                Die Antwort darf KEIN XML, HTML, Markdown, Codeblöcke
                oder sonstige Markup-Strukturen enthalten.
                Verwende ausschließlich normalen Fließtext.
                """
        ));


        conversation.add(new OllamaMessage(
                "user",
                """
                Fasse mir den Inhalt dieses österreichischen Gesetzes im XML
                kurz und einfach zusammen.
                Konzentriere dich auf Rechte und Pflichten von Privatpersonen
                und deren Auswirkungen.
                Antworte ausschließlich mit der Zusammenfassung selbst.
                """
        ));
        conversation.add(new OllamaMessage(
                "user",
                "Der folgende Text ist auf Deutsch."
        ));
        conversation.add(new OllamaMessage(
                "user",
                """
                Gib die Zusammenfassung als normalen Fließtext
                in ganzen Sätzen aus.
                Keine Aufzählungen, keine Formatierung.
                """
        ));

        conversation.add(new OllamaMessage(
                "user",
                xml
        ));
        String answer = chat(conversation);
        // Antwort im Kontext speichern!
        conversation.add(new OllamaMessage("assistant", answer));
        System.out.println(answer);
        ld.summary = answer;

        List<OllamaMessage> timeMsg = fork(conversation);
        timeMsg.add(new OllamaMessage(
                "user",
                "Es ist aktuell "+time+". Sag mir sehr kurz welchen einfluss die aktuelle Uhrzeit, bezogen auf das Gesetz, auf mich hat"
        ));
        String followUp = chat(timeMsg);
        timeMsg.add(new OllamaMessage("assistant", followUp));
        ld.summary += "\n---------\n" + followUp;


        List<OllamaMessage> kanzlei = fork(conversation);
        kanzlei.add(new OllamaMessage(
                "user",
                """
                Gib mir zu diesem österreichischen Gesetz passend
                genau eine fiktive lustige Anwaltskanzlei.
                Nur Name und kurzes Motto, sonst nichts.
                """
        ));
        String lawyer = chat(kanzlei);
        //conversation.add(new OllamaMessage("assistant", lawyer));
        ld.lawyer = lawyer;

    }


    String summarizeBgblXml(String xml) throws Exception
    {
        OllamaClient oc = OllamaClient.getOllamaClient();

        String prePrompt = " Fasse mir den Inhalt dieses österreichischen Gesetzes im XML kurz und einfach zusammen und beziehe dich vor allem auf Punkte, die sich auf die Rechte bzw. Pflichten von Privatpersonen beziehen, und welche auswirkungen sie auf mich haben. Antworte ausschließlich mit der Zusammenfassung selbst. Keine Einleitung, keine Wiederholung der Aufgabe, keine Links, keine Disclaimer. Die Zusammenfassung darf ansprechend gestaltet sein. ";

        String prompt = prePrompt + xml + "\nDie Antwort muss auf Deutsch sein!";
        System.out.println(prompt);
        String answer = oc.ask(/*model,*/ prompt);

        return answer;
    }

    String getFunfact() throws Exception
    {
        return ask("Gib mir einen sehr Kurzen Funfact über das Österreichische Gesetz. Keine Einleitung, keine Wiederholung der Aufgabe, keine Links, keine Disclaimer.");
    }


}
