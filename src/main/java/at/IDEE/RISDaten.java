package at.IDEE;

public class RISDaten
{
    static LawDetailsShort getShortData(String search)
    {
        System.out.println("getShortLaws");
        System.out.println("search: " + search);
        LawDetailsShort shortLaw = new LawDetailsShort();
        LawDetailShort sl1 = new LawDetailShort();
        LawDetailShort sl2 = new LawDetailShort();
        if (search.equals("Recht"))
        {
            sl1.id = "§1";
            sl1.title = "Das Recht zu schweigen";

            sl2.id = "§2";
            sl2.title = "Das Recht auf einen anwalt";
        }
        else
        {
            sl1.id = "§3";
            sl1.title = "Das Recht auf Ruhe";

            sl2.id = "§4";
            sl2.title = "Das Recht auf Party";
        }
        shortLaw.lawDetailShort.add(sl1);
        shortLaw.lawDetailShort.add(sl2);

        return shortLaw;
    }

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
