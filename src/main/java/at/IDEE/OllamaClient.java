package at.IDEE;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OllamaClient
{
    private final HttpClient httpClient;
    private final String baseUrl;
    private final String API = "/api/";;
    private final String GENERATE = API + "generate";
    private final String MODEL = "phi3";

    private Gson gson = new Gson();

    public OllamaClient()
    {
        this("http://localhost:11434");
    }
    public OllamaClient(String baseUrl)
    {
        this.httpClient = HttpClient.newHttpClient();
        this.baseUrl = baseUrl;
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
                .uri(URI.create(baseUrl+ GENERATE))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null)
        {
            //jedes Chunk ist ein JSON Object
            //System.out.println(line);
            OllamaResponse resp = gson.fromJson(line, OllamaResponse.class);
            sb.append(resp.response);
        }

        return sb.toString();
    }


}
