package yukkyna.itunesearcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Web上の画像表示に対応したImageView
 */
public class WebImageView extends ImageView {

    private String webUrl;
    private DownloadTask task;

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
        this.setImageResource(R.drawable.loading);
        if (this.task != null) {
//            task.cancel(false);
            task.cancel(true);
        }
        this.task = new DownloadTask();
        task.execute(url);
    }

    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                // TODO cancelするとBitmapFactory.decodeStreamでInterruptedIOExceptionが発生する
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
//            } catch (Exception e) {
//                e.printStackTrace();
            } catch (MalformedURLException e) {
//                e.printStackTrace();
            } catch (InterruptedIOException e) {

            } catch (IOException e) {
//                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.d("WebImageView", "onPostExecute");
            if (bitmap != null) {
                setImageBitmap(bitmap);
            } else {
                setImageResource(R.drawable.loading);
            }
        }
    }
}
