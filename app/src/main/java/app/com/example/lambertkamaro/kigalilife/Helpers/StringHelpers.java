package app.com.example.lambertkamaro.kigalilife.Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
/**
 * Created by Lambert.Kamaro on 1/7/2016.
 */
public class StringHelpers {

  public   ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray(jsonString).getJSONArray(0);

        for (int i = 0; i < jsonArray.length(); i++) {;
            stringArray.add(jsonArray.getString(i));
        }

        return stringArray;
    }
}
