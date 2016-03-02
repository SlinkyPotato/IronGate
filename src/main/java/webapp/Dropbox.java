package webapp;

import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

/**
 * Dropbox API specific
 */
public class Dropbox {
    // retrieve private credentials from Dropbox Developers app
    private String key;
    private String secret;
    private String token;

    public Dropbox() {
        readSetCredentials();
    }
    public Dropbox(String key, String secret) {
        this.key = key;
        this.secret = secret;
    }
    public Dropbox(String key, String secret, String token) {
        this.key = key;
        this.secret = secret;
        this.token = token;
    }

    private void readSetCredentials() {
        try {
            URL url = getClass().getResource("/services/dropbox.json");
            FileReader readCred = new FileReader(url.getPath());
            JSONObject dropCred = new JSONObject(url.getPath());
            key = dropCred.getString("key");
            secret = dropCred.getString("secret");
            token = dropCred.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getKey() {
        return key;
    }
    public String getSecret() {
        return secret;
    }
    public String getToken() {
        return token;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public void setToken(String token) {
        this.token = token;
    }


}
