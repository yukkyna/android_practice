package yukkyna.itunesearcher;

import android.app.LoaderManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by yukkyna on 2015/01/03.
 */
public class CustomListItemAdapter extends ArrayAdapter<ItemDto> {

    private LayoutInflater mLayoutInflater;

    public CustomListItemAdapter(Context context, List<ItemDto> items) {
        super(context, 0, items);

        // レイアウトをxmlから読み込むときはインフレーターを使う
        this.mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // ListViewに表示する分のレイアウトが生成されていない場合レイアウトを作成する
        if (convertView == null) {
            view = this.mLayoutInflater.inflate(R.layout.custom_listitem, parent, false);
        } else {
            // レイアウトが存在する場合は再利用する
            view = convertView;
        }

        ItemDto item = getItem(position);
        TextView text1 = (TextView)view.findViewById(R.id.listitem_title);
        text1.setText(item.trackName);
        WebImageView iv = (WebImageView)view.findViewById(R.id.listitem_webthumb);
        iv.setWebUrl(item.artworkUrl100);


//        LoaderManager manager = this.
//        try {
//            // TODO 後始末
//            URL url = new URL(item.artworkUrl100);
//            InputStream is = url.openStream();
//            Bitmap d = BitmapFactory.decodeStream(is);
//            is.close();
//            iv.setImageBitmap(d);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return view;
    }

}
