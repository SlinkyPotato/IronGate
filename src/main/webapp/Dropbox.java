package main.webapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Dropbox API specific
 */
public class Dropbox {
    // retrieve private credentials from Dropbox Developers app
    static {
        try {
            FileReader readCred = new FileReader("dropbox.json");
            JSONObject dropCred = new JSONObject(readCred);
            final String key = dropCred.getString("key");
            final String secret = dropCred.getString("secret");
            final String token = dropCred.getString("token");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
