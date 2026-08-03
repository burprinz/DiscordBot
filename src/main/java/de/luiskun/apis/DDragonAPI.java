package de.luiskun.apis;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DDragonAPI {

    public static String getNewestAPIVersion() {
        JsonArray obj = Requester.requestAsList(HTTPLinks.DDRAGON_VERSIONS).get("list").getAsJsonArray();
        return obj.get(0).getAsString();
    }

}
