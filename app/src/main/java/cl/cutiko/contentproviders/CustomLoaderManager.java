package cl.cutiko.contentproviders;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by cutiko on 08-03-17.
 */

public class CustomLoaderManager implements LoaderManager.LoaderCallbacks {

    private static final int CUSTOM_LOADER_ID = 343;
    public static final String URI = "cl.cutiko.contentproviders.CustomLoaderManager.URI";
    public static final String PROJECTION = "cl.cutiko.contentproviders.CustomLoaderManager.PROJECTION";
    public static final java.lang.String SELECTION = "cl.cutiko.contentproviders.CustomLoaderManager.SELECTION";
    public static final String ARGUMENTS = "cl.cutiko.contentproviders.CustomLoaderManager.ARGUMENTS";
    public static final String SORT = "cl.cutiko.contentproviders.CustomLoaderManager.SORT";
    private final AppCompatActivity activity;
    private final LoaderManager loaderManager;

    public CustomLoaderManager(AppCompatActivity activity, Bundle args) {
        this.activity = activity;
        loaderManager = activity.getSupportLoaderManager();
        loaderManager.initLoader(CUSTOM_LOADER_ID, args, this);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.d("onCreateLoader", "id: " + String.valueOf(id));
        Uri uri = Uri.parse(args.getString(URI));
        String[] projection = args.getStringArray(PROJECTION);
        String selection = args.getString(SELECTION);
        String[] selectionArgs = args.getStringArray(ARGUMENTS);
        String sort = args.getString(SORT);
        return new CursorLoader(activity, uri, projection, selection, selectionArgs, sort);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        /*if something then restart the loader
        loaderManager.restartLoader(CUSTOM_LOADER_ID, null, this);*/
        if (data != null) {
            Log.d("onLoadFinished", "data is not null");
        } else {
            Log.d("onLoadFinished", "is not null");
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
