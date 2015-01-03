package yukkyna.itunesearcher;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yukkyna on 2015/01/03.
 */
public class SearchAsyncTaskLoader extends AsyncTaskLoader<ItemList> {

    private ItemList mCachedData;

    public SearchAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public ItemList loadInBackground() {
        Log.d("SearchAsyncTaskLoader", "loadInBackground");
        URL url = null;
        HttpURLConnection con = null;
        InputStreamReader isr = null;
        try {
            url = new URL("https://itunes.apple.com/search?term=babymetal");
            con = (HttpURLConnection)url.openConnection();
            con.connect();
            InputStream is = con.getInputStream();

            isr = new InputStreamReader(is);
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[1024];
            int len;
            while (true) {
                len = isr.read(buf);
                if (len < 0) {
                    break;
                }
                sb.append(buf, 0, len);
            }

            // レスポンスをパーすする
            ItemList list = new ItemList();
            JSONObject json = new JSONObject(sb.toString());
            int num = json.getInt("resultCount");
            JSONArray results = json.getJSONArray("results");
            for (int i = 0; i < num; i ++) {
                list.add(new ItemDto(results.getJSONObject(i)));
            }
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }

        return null;
    }

    @Override
    public void deliverResult(ItemList data) {
        // ローダがリセットされ、そのローダのライフサイクルが終了となる場合
        if (isReset()) {
            // キャッシュデータがある場合は、キャッシュを削除して、メモリから破棄可能にする
            if (mCachedData != null) {
                mCachedData = null;
            }
            return;
        }

        // 得られたデータをキャッシュする
        mCachedData = data;

        // ローダが開始されている場合、親にデータが得られたことを通知する
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        // キャッシュがある場合はそちらを返す
        if (mCachedData != null) {
            deliverResult(mCachedData);
            return;
        }

        // データソースに変更があったり、キャッシュデータがない場合は loadInBackground() に行くようにする
        if (takeContentChanged() || mCachedData == null) {
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
