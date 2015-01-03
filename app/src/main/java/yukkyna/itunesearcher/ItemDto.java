package yukkyna.itunesearcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by yukkyna on 2015/01/03.
 */
public class ItemDto {
    public String trackName;
    public Bitmap artworkUrl100;

    public ItemDto(JSONObject o) throws JSONException, URISyntaxException, IOException {
        this.trackName = o.getString("trackName");

        // TODO 後始末
        URL url = new URL(o.getString("artworkUrl100"));
        InputStream is = url.openStream();
        Bitmap d = BitmapFactory.decodeStream(is);
        is.close();

        this.artworkUrl100 = d;
    }
}
