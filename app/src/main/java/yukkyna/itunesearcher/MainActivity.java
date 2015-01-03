package yukkyna.itunesearcher;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<ItemList>, SearchView.OnQueryTextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SearchView sv = (SearchView)this.findViewById(R.id.searchView);
        sv.setOnQueryTextListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ---------------------------------------------------------------------------------------------
    // Search View
    // ---------------------------------------------------------------------------------------------
    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("onQueryTextSubmit", query);

        // ネットワーク状態を取得する
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info.isConnected()) {
//            Toast.makeText(this, info.getTypeName() + " connected", Toast.LENGTH_LONG).show();

            TextView tv = (TextView)this.findViewById(R.id.textView);
            tv.setText(this.getString(R.string.search_result) + query);

            // iTune Search APIにリクエストする
            LoaderManager manager = getSupportLoaderManager();
            Bundle bundle = new Bundle();
            manager.initLoader(0, bundle, MainActivity.this);
        } else {
            Toast.makeText(this, "isConnected = false", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    // ---------------------------------------------------------------------------------------------
    // Loader
    // ---------------------------------------------------------------------------------------------
    @Override
    public Loader<ItemList> onCreateLoader(int i, Bundle bundle) {
//        Log.d("", "onCreateLoader");
        if (i == 0) {
//            Log.d("", "create task");
            return new SearchAsyncTaskLoader(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ItemList> objectLoader, ItemList o) {
        Log.d("MainActivity", "onLoadFinished");
        if (o != null) {
            int num = o.size();
            for (int i = 0; i < num; i ++) {
                Log.d("", o.get(i).trackName);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<ItemList> objectLoader) {

    }
}
