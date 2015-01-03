package yukkyna.itunesearcher;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yukkyna on 2015/01/03.
 */
public class ItemDto {
    public String trackName;

    public ItemDto(JSONObject o) throws JSONException {
        this.trackName = o.getString("trackName");
    }
}
