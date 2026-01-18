package at.IDEE;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

public class Main {
    public static void main(String[] args) throws IOException
    {
        System.out.println("Starte IDEE Server");

        int port = 6000; // Portnummer
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new IdeeHttpServer());
        server.setExecutor(null); // Standard-Executor verwenden
        server.start();
    }

}