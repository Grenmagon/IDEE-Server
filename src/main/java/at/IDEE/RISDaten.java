package at.IDEE;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RISDaten
{
    static LawDetailsShort getShortDataBgAuth(String search)
    {
        System.out.println("getShortLaws");
        System.out.println("search: " + search);
        LawDetailsShort shortLaw = new LawDetailsShort();

        try {
            RISApiClient client = new RISApiClient();
            String jsonResponse = client.searchBgblAuth(search);
            JsonObject responseObj = client.parseResponse(jsonResponse)
                    .getAsJsonObject("OgdSearchResult")
                    .getAsJsonObject("OgdDocumentResults");

            JsonObject hitsObj = responseObj.getAsJsonObject("Hits");

            String hitsText = hitsObj.get("#text").getAsString();
            int numHits = Integer.parseInt(hitsText);

            System.out.println();
            System.out.println("Hits:" + numHits);

            if (numHits > 0) {
                if (responseObj.has("OgdDocumentReference")) {
                    JsonElement refElement = responseObj.get("OgdDocumentReference");
                    JsonArray results;

                    if (refElement.isJsonArray()) {
                        results = refElement.getAsJsonArray();
                    } else {
                        // In case the API returns a single object instead of an array for 1 hit
                        results = new JsonArray();
                        results.add(refElement);
                    }

                    System.out.println("Result size: " + results.size());
                    for (JsonElement element : results) {
                        JsonObject item = element.getAsJsonObject();
                        //System.out.println(item.toString());
                        LawDetailShort sl = new LawDetailShort();

                        if (item.has("Data")) {
                            JsonObject data = item.getAsJsonObject("Data");

                            if (data.has("Metadaten")) {
                                JsonObject meta = data.getAsJsonObject("Metadaten");
                                if (meta.has("Technisch")) {
                                    JsonObject tech = meta.getAsJsonObject("Technisch");
                                    sl.dokid = tech.has("ID") ? tech.get("ID").getAsString() : "";
                                    sl.url = "https://www.ris.bka.gv.at/Dokumente/BgblAuth/" + sl.dokid + "/" + sl.dokid + ".pdf";
                                }
                                if (meta.has("Bundesrecht")) {
                                    JsonObject bund = meta.getAsJsonObject("Bundesrecht");
                                    sl.title = bund.has("Kurztitel") ? bund.get("Kurztitel").getAsString() : "";

                                    if (bund.has("BgblAuth")) {
                                        JsonObject bgbl = bund.getAsJsonObject("BgblAuth");
                                        sl.id = bgbl.has("Bgblnummer") ? bgbl.get("Bgblnummer").getAsString() : "";
                                    }
                                }
                            }
                        }
                        shortLaw.lawDetailShort.add(sl);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shortLaw;
    }

    static LawDetailsShort getShortDataBundesrecht(String search)
    {
        System.out.println("getShortLaws");
        System.out.println("search: " + search);
        LawDetailsShort shortLaw = new LawDetailsShort();

        try {
            RISApiClient client = new RISApiClient();
            String jsonResponse = client.searchBundesgesetzAuth(search);

            JsonObject responseObj = client.parseResponse(jsonResponse)
                    .getAsJsonObject("OgdSearchResult")
                    .getAsJsonObject("OgdDocumentResults");

            JsonObject hitsObj = responseObj.getAsJsonObject("Hits");

            String hitsText = hitsObj.get("#text").getAsString();
            int numHits = Integer.parseInt(hitsText);

            System.out.println();
            System.out.println("Hits:" + numHits);

            if (numHits > 0) {
                if (responseObj.has("OgdDocumentReference")) {
                    JsonElement refElement = responseObj.get("OgdDocumentReference");
                    JsonArray results;

                    if (refElement.isJsonArray()) {
                        results = refElement.getAsJsonArray();
                    } else {
                        // In case the API returns a single object instead of an array for 1 hit
                        results = new JsonArray();
                        results.add(refElement);
                    }

                    System.out.println("Result size: " + results.size());
                    for (JsonElement element : results) {
                        JsonObject item = element.getAsJsonObject();
                        //System.out.println(item.toString());
                        LawDetailShort sl = new LawDetailShort();

                        if (item.has("Data")) {
                            JsonObject data = item.getAsJsonObject("Data");

                            if (data.has("Metadaten")) {
                                JsonObject meta = data.getAsJsonObject("Metadaten");
                                if (meta.has("Technisch")) {
                                    JsonObject tech = meta.getAsJsonObject("Technisch");
                                    sl.dokid = tech.has("ID") ? tech.get("ID").getAsString() : "";
                                    sl.url = "https://www.ris.bka.gv.at/Dokumente/Bundesnormen/" + sl.dokid + "/" + sl.dokid + ".pdf";
                                }
                                if (meta.has("Bundesrecht")) {

                                        JsonObject bund = meta.getAsJsonObject("Bundesrecht");
                                        sl.title = bund.has("Kurztitel") ? bund.get("Kurztitel").getAsString() : "";

                                        if (bund.has("BrKons"))
                                        {
                                            JsonObject bgbl = bund.getAsJsonObject("BrKons");

                                            sl.id = bgbl.has("ArtikelParagraphAnlage") ? bgbl.get("ArtikelParagraphAnlage").getAsString() : "";
                                        }
                                }
                            }
                        }

                            shortLaw.lawDetailShort.add(sl);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return shortLaw;
    }

    // LawDetailsShort test method
    public static void main(String[] args) {
        System.out.println("=== Testing RIS API Integration ===");
        AskLawDetail ald = new AskLawDetail();
        ald.id = "NOR40004861";

        getLaw(ald);
        ald.id = "NOR12018827";
        getLaw(ald);
    }

    private static String buildLawUrlPre(String id)
    {
        String BaseURL = "https://www.ris.bka.gv.at/Dokumente/Bundesnormen/";
        String url = BaseURL + id + "/" + id + ".";
        return url;
    }
    static String buildLawUrlText(String id)
    {
        return buildLawUrlPre(id) + "rtf";
    }
    static String buildLawUrlXml(String id)
    {
        String url = buildLawUrlPre(id) + "xml";
        System.out.println(url);
        return url;
    }
    static String buildLawUrlPDF(String id)
    {
        return buildLawUrlPre(id) + "pdf";
    }
    static String buildLawUrlHtml(String id)
    {
        return buildLawUrlPre(id) + "html";
    }

    static String getLawXML(String id) throws Exception
    {
        RISApiClient client = new RISApiClient();
        return client.loadLaw(buildLawUrlXml(id));

    }
    static String getLawHtml(String id) throws Exception
    {
        RISApiClient client = new RISApiClient();
        return client.loadLaw(buildLawUrlHtml(id));
    }
    static String getLawText(String id) throws Exception
    {
        RISApiClient client = new RISApiClient();
        return client.loadLaw(buildLawUrlText(id));
    }
    static String getLawPdf(String id) throws Exception
    {
        RISApiClient client = new RISApiClient();
        return client.loadLaw(buildLawUrlPDF(id));
    }

    static LawDetail getLaw(AskLawDetail ald)
    {
        LawDetail ld = new LawDetail();

        ld.id = ald.id;
        ld.officialText = RISDaten.buildLawUrlPDF(ld.id);

        ld.title = "Error";
        ld.paragraph = "Error";
        ld.category = "Empty";
        ld.summary = "Leider konnten wir unsere klugen Helfer nicht erreichen! :(";
        ld.lawyer = "Kanzlei Fehlerfrei\nDen Fehler passiert nur anderen!";
        ld.source = "RIS: www.ris.bka.gv.at/Bgbl-Auth/";

        try
        {
            RISApiClient client = new RISApiClient();
            String jsonResponse = client.searchBundesgesetzAuth(ld.id);
            JsonObject responseObj = client.parseResponse(jsonResponse)
                .getAsJsonObject("OgdSearchResult").getAsJsonObject("OgdDocumentResults");

            JsonObject hitsObj = responseObj.getAsJsonObject("Hits");

            String hitsText = hitsObj.get("#text").getAsString();
            int numHits = Integer.parseInt(hitsText);

            System.out.println();
            System.out.println("Hits:" + numHits);

            if (numHits > 0)
            {
                if (responseObj.has("OgdDocumentReference"))
                {
                    JsonElement refElement = responseObj.get("OgdDocumentReference");
                    JsonArray results;

                    if (refElement.isJsonArray())
                    {
                        results = refElement.getAsJsonArray();
                    }
                    else
                    {
                        // In case the API returns a single object instead of an array for 1 hit
                        results = new JsonArray();
                        results.add(refElement);
                    }

                    System.out.println("Result size: " + results.size());
                    for (JsonElement element : results)
                    {
                        JsonObject item = element.getAsJsonObject();
                        if (item.has("Data"))
                        {
                            JsonObject data = item.getAsJsonObject("Data");

                            if (data.has("Metadaten"))
                            {
                                JsonObject meta = data.getAsJsonObject("Metadaten");
                                if (meta.has("Bundesrecht")) {
                                    JsonObject bund = meta.getAsJsonObject("Bundesrecht");
                                    System.out.println(bund.toString());

                                    ld.title = bund.has("Kurztitel") ? bund.get("Kurztitel").getAsString() : "";
                                    if (bund.has("BrKons")) {
                                        JsonObject bgbl = bund.getAsJsonObject("BrKons");
                                        String para  =  bgbl.has("Abkuerzung") ? bgbl.get("Abkuerzung").getAsString() + " " : "";
                                        para += bgbl.has("ArtikelParagraphAnlage") ? bgbl.get("ArtikelParagraphAnlage").getAsString() : "";

                                        if (!para.isEmpty())
                                            ld.paragraph = para;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        try
        {
            String xml = RISDaten.getLawXML(ld.id);
            OllamaClient.getOllamaClient().summarizeBgBl(ld, xml, ald.datetime);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return ld;
    }


}
