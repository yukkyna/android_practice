package yukkyna.itunesearcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by yukkyna on 2015/01/03.
 */
public class ThumbnailAsyncTaskLoader extends AsyncTaskLoader<Bitmap> {

    private Bitmap cachedData;
    private String url;

    public ThumbnailAsyncTaskLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public Bitmap loadInBackground() {
        try {
            // TODO 後始末
            URL url = new URL(this.url);
            InputStream is = url.openStream();
            Bitmap d = BitmapFactory.decodeStream(is);
            is.close();
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deliverResult(Bitmap data) {
        // ローダがリセットされ、そのローダのライフサイクルが終了となる場合
        if (isReset()) {
            // キャッシュデータがある場合は、キャッシュを削除して、メモリから破棄可能にする
            if (this.cachedData != null) {
                this.cachedData = null;
            }
            return;
        }

        // 得られたデータをキャッシュする
        this.cachedData = data;

        // ローダが開始されている場合、親にデータが得られたことを通知する
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        // キャッシュがある場合はそちらを返す
        if (this.cachedData != null) {
            deliverResult(this.cachedData);
            return;
        }

        // データソースに変更があったり、キャッシュデータがない場合は loadInBackground() に行くようにする
        if (takeContentChanged() || this.cachedData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
        super.onStopLoading();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        super.onReset();
    }
}
