package at.IDEE;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class IdeeHttpServer implements HttpHandler
{
    private static final String API = "/API/";

    class ExchangeValues
    {
        public boolean status = true;

        HttpExchange exchange;
        Map<String, String> params;

        ExchangeValues(HttpExchange exchange) throws IOException
        {
            this.exchange = exchange;
            params = parseFormData(exchange);
        }

    }

    public static Map<String, String> parseFormData(HttpExchange exchange) throws IOException
    {
        Map<String, String> params = new HashMap<>();

        // 1. GET-Parameter aus URL
        String query = exchange.getRequestURI().getRawQuery();
        if (query != null)
        {
            parseQuery(query, params);
        }

        // 2. Body-Parameter aus POST, PUT oder DELETE
        String method = exchange.getRequestMethod().toUpperCase();
        if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE"))
        {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            //System.out.println("body: '" + body + "'");
            parseQuery(body, params);

        }

        /*
        //System.out.println("Form data:");
        for (Map.Entry<String, String> entry : params.entrySet())
        {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        */


        return params;
    }

    public static void parseQuery(String query, Map<String, String> params) throws UnsupportedEncodingException
    {
        if (query == null || query.isEmpty()) return;

        String[] pairs = query.split("&");
        for (String pair : pairs)
        {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "";
            params.put(key, value);
        }
    }

    private void sendResponseString(HttpExchange exchange, int statusCode, String response) throws IOException
    {
        System.out.println("sendResponseString");
        sendResponse(exchange, statusCode, response.getBytes());
    }

    private void sendResponse(HttpExchange exchange, int statusCode, byte[] response) throws IOException
    {
        System.out.println("sendResponse");
        String normalizedPath = getNormalizedPath(exchange);
        String contentType = guessContentType(normalizedPath); // statt exchange.getRequestURI().getPath()

        exchange.getResponseHeaders().set("Content-Type", contentType);


        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, response.length);

        try (OutputStream os = exchange.getResponseBody())
        {
            os.write(response);
            os.flush();
        }
    }

    private String guessContentType(String path)
    {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }

    public static String getNormalizedPath(HttpExchange exchange)
    {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/"))
            path += "index.html";
        return path;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        String urlPath = getNormalizedPath(exchange);
        System.out.println("handle: " + urlPath);

        ExchangeValues ev = new ExchangeValues(exchange);
        String method = exchange.getRequestMethod();
        if (method.equals("GET"))
        {
            if (doGet(ev))
                return;
        }
    }

    public boolean doGet(ExchangeValues ev) throws IOException
    {
        System.out.println("DoGet");
        String urlPath = getNormalizedPath(ev.exchange);

        if (urlPath.startsWith(API + "categories.json"))
        {
            getCategories(ev);
            return true;
        }
        else if (urlPath.startsWith(API + "funfact.json"))
        {
            getFunfact(ev);
            return true;
        }
        else if (urlPath.startsWith(API + "LawDetailsShort.json"))
        {
            getShortLaws(ev);
            return true;
        }
        else if (urlPath.startsWith(API + "LawDetail.json"))
        {
            getLawDetail(ev);
            return true;
        }
        else if (urlPath.startsWith(API + "QuizQuestions.json"))
        {
            getQuizQuestions(ev);
            return true;
        }
        else
            System.out.println("Nicht gefunden!");
        return false;
    }

    private void getCategories(ExchangeValues ev) throws IOException
    {
        System.out.println("getCategories");
        CategoriesAnswer json = new CategoriesAnswer();
        json.categories.add("Recht");
        json.categories.add("Wohnen");

        Gson gson = new Gson();
        sendResponseString(ev.exchange, 200, gson.toJson(json));

    }

    private void getFunfact(ExchangeValues ev) throws IOException
    {
        Gson gson = new Gson();
        sendResponseString(ev.exchange, 200, gson.toJson(ExtraContent.getFunfact()));
    }

    private void getShortLaws(ExchangeValues ev) throws IOException
    {
        Gson gson = new Gson();
        sendResponseString(ev.exchange, 200, gson.toJson(RISDaten.getShortData(ev.params.get("category"))));
    }

    private void getLawDetail(ExchangeValues ev) throws IOException
    {
        Gson gson = new Gson();
        sendResponseString(ev.exchange, 200, gson.toJson(RISDaten.getLawDetail(ev.params.get("id"))));
    }

    private void getQuizQuestions(ExchangeValues ev) throws IOException
    {
        Gson gson = new Gson();
        sendResponseString(ev.exchange, 200, gson.toJson(ExtraContent.getQuizQestions()));
    }

}
