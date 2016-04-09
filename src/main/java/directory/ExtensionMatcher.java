package directory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Takes an extension of a file and finds related tags
 * Created by kristopherguzman on 4/8/16.
 */
public class ExtensionMatcher {
    private JSONObject json;

    public ExtensionMatcher() {
        try {
            String jsonPath = getClass().getClassLoader().getResource("userData/extensions.json").getPath();
            BufferedReader reader = new BufferedReader(new FileReader(jsonPath));
            String line;
            String jsonString = "";
            while((line = reader.readLine()) != null) {
                jsonString += line + "\n";
            }
            reader.close();
            System.out.println("json: " + jsonString);
            json = new JSONObject(jsonString);
        } catch(Exception e) { System.out.println("CATCH ERROR"); e.printStackTrace(); }

    }

    public List<String> getRelated(String ext) {
        System.out.println("EXTENSION: " + ext);
        List<String> related = new ArrayList<>();
        try {
            JSONArray array = (JSONArray) json.get(ext);
            array.forEach(obj -> related.add((String) obj));
        } catch(Exception e) { System.out.println("no matching extension for " + ext); }
        return related;
    }
}
