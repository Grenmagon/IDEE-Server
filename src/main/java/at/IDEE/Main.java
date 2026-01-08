package at.IDEE;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException
    {
        OllamaClient oc = new OllamaClient();

        try
        {
            System.out.println("ASK Ollama:");
            //System.out.println(oc.ask("phi3", "Hallo"));
            //testLLM(oc);
            System.out.println("FERTIG");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("Starte IDEE Server");
        int port = 6000; // Portnummer
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new IdeeHttpServer());
        server.setExecutor(null); // Standard-Executor verwenden
        server.start();
    }

    static void testLLM(OllamaClient oc) throws Exception
    {
        String[] gs = new String[]{
                "§ 514. Wenn der Eigenthümer Bauführungen, die durch das Alter des Gebäudes, oder durch einen Zufall nothwenig gemacht werden, auf Anzeige des Fruchtnießers auf seine Kosten besorgt; ist ihm der Fruchtnießer, nach Maß der dadurch verbesserten Fruchtnießung, die Zinsen des verwendeten Capitals zu vergüten schuldig.",
                " (1)Die Erbfähigkeit muss im Zeitpunkt des Erbanfalls vorliegen. Eine später erlangte Erbfähigkeit ist unbeachtlich und berechtigt daher nicht, anderen das zu entziehen, was ihnen bereits rechtmäßig zugekommen ist.\n" +
                        "(2)Wer nach dem Erbanfall eine gerichtlich strafbare Handlung gegen die Verlassenschaft im Sinn des § 539 begeht oder die Verwirklichung des wahren letzten Willens des Verstorbenen vereitelt oder zu vereiteln versucht (§ 540), verliert nachträglich seine Erbfähigkeit.",
                "(1)Absatz 1 Die Ehegatten sind einander zur umfassenden ehelichen Lebensgemeinschaft, besonders zum gemeinsamen Wohnen, sowie zur Treue, zur anständigen Begegnung und zum Beistand verpflichtet.\n" +
                        "(2)Absatz 2 Im Erwerb des anderen hat ein Ehegatte mitzuwirken, soweit ihm dies zumutbar, es nach den Lebensverhältnissen der Ehegatten üblich und nicht anderes vereinbart ist.\n" +
                        "(3)Absatz 3 Jeder Ehegatte hat dem anderen in der Ausübung der Obsorge für dessen Kinder in angemessener Weise beizustehen. Soweit es die Umstände erfordern, vertritt er ihn auch in den Obsorgeangelegenheiten des täglichen Lebens."
        };

        String prePrompt= "Fasse mir diese österreichische Gesetz kurz und einfach zusammen: ";
        String[] llm = new String[]{"phi3", "gemma3", "gemma3:12b", "mistral", "llama3.2"};

        for(String l: llm)
        {
            System.out.println(l);
            System.out.println("##############################");
            System.out.println();
            for (String g: gs)
            {
                String prompt = prePrompt + g;
                System.out.println(prompt);
                long start = System.currentTimeMillis();
                System.out.println(oc.ask(l, prompt));
                long end = System.currentTimeMillis();
                long dauer = end - start;
                double dauerSek = dauer / 1000.0;
                System.out.println("Dauer: " + dauerSek + "s");
                System.out.println("----------------------------------------------------------------");
                System.out.println();
            }
        }
    }
}