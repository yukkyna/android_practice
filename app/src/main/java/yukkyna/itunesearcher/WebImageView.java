package yukkyna.itunesearcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * TODO: document your custom view class.
 */
public class WebImageView extends ImageView {

    private String webUrl;

    public WebImageView(Context context) {
        super(context);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setWebUrl(String url) {
        this.webUrl = url;
        DownloadTask task = new DownloadTask();
        task.execute(url);
    }

    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                InputStream is = url.openStream();
                Bitmap d = BitmapFactory.decodeStream(is);
                is.close();
                if (webUrl.equals(params[0])) {
                    return d;
                }

//                URLConnection con = url.openConnection();
//                BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
//                ByteArrayBuffer baf = new ByteArrayBuffer();
//                int current = 0;
//                while ((current = bis.read()) != -1) {
//                    baf.append((byte) current);
//                }
//                byte[] imageData = baf.toByteArray();
//                return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.d("WebImageView", "onPostExecute");
            setImageBitmap(bitmap);
//            mImage = new BitmapDrawable(bitmap);
//            if (mImage != null) {
//                setImageDrawable(mImage);
//            }
        }
    }
}
