package at.IDEE;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.net.ssl.*;

public class RISApiClient {

    private static final String BASE_URL = "https://data.bka.gv.at/ris/api/v2.6/Bundesrecht";
    private final HttpClient httpClient;
    private final Gson gson;

    public RISApiClient() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        this.httpClient = HttpClient.newBuilder()
                .sslContext(sslContext)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.gson = new Gson();
    }

    public String searchBgblAuth(String searchTerm) throws Exception {
        String encodedSearchTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
        String url = String.format("%s?Applikation=BgblAuth&Suchworte=%s&SucheInTeil1=true",
                BASE_URL, encodedSearchTerm);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("API Error: " + response.statusCode() + " - " + response.body());
        }
    }

    public JsonObject parseResponse(String jsonResponse) {
        return gson.fromJson(jsonResponse, JsonObject.class);
    }

   //  main method for testing
//    public static void main(String[] args) {
//        try {
//            RISApiClient client = new RISApiClient();
//
//            String result = client.searchBgblAuth("eheschließung");
//            System.out.println(result);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}