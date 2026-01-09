package at.IDEE;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RISDaten
{
    static LawDetailsShort getShortData(String search)
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

    // LawDetailsShort test method
//    public static void main(String[] args) {
//        System.out.println("=== Testing RIS API Integration ===");
//        LawDetailsShort results = getShortData("Mieter");
//
//        if (results.lawDetailShort.isEmpty()) {
//            System.out.println("No results found or error occurred.");
//        } else {
//            System.out.println("Found " + results.lawDetailShort.size() + " laws:");
//            for (LawDetailShort sl : results.lawDetailShort) {
//                System.out.println("ID: " + sl.id);
//                System.out.println("Titel: " + sl.title);
//                System.out.println("URL: " + sl.url);
//                System.out.println("-------------------------");
//            }
//        }
//    }


    //Summary wird noch an den Ollama Client geschickt -> am anfang leer lassen oder mit dummy data füllen
    static LawDetail getLawDetail(String id)
    {
        System.out.println("getLawDetail");
        LawDetail lawDetail = new LawDetail();
        if (id.equals("§1"))
        {
            lawDetail.id = id;
            lawDetail.title = "Das Recht zu schweigen";
            lawDetail.category = "Recht";
            lawDetail.paragraph = id;
            lawDetail.summary = "Red nicht mit Cops";
            lawDetail.officialText = "Du brauchst dich nicht selbst kriminalisieren";
            lawDetail.source = "I made it the fuck up1";
        }
        else if (id.equals("§2"))
        {
            lawDetail.id = id;
            lawDetail.title = "Das Recht auf einen anwalt";
            lawDetail.category = "Recht";
            lawDetail.paragraph = id;
            lawDetail.summary = "Ein Anwalt wird dir Helfen";
            lawDetail.officialText = "Du hast anrecht auf einen Anwalt";
            lawDetail.source = "I made it the fuck up2";
        }
        else if (id.equals("§3"))
        {
            lawDetail.id = id;
            lawDetail.title = "Das Recht auf Ruhe";
            lawDetail.category = "Wohnen";
            lawDetail.paragraph = id;
            lawDetail.summary = "Nachbarn müssen ruhig sein";
            lawDetail.officialText = "Es darf in Wohnungen nicht zu laut sein";
            lawDetail.source = "I made it the fuck up3";
        }
        else if (id.equals("§4"))
        {
            lawDetail.id = id;
            lawDetail.title = "Das Recht auf Party";
            lawDetail.category = "Wohnen";
            lawDetail.paragraph = id;
            lawDetail.summary = "PAARTEYY";
            lawDetail.officialText = "Du drafst party machen";
            lawDetail.source = "I made it the fuck up4";
        }
        else {
            lawDetail.id = "0";
            lawDetail.title = "unbekannt";
        }
        return lawDetail;
    }
}
