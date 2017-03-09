package cl.cutiko.contentproviders;

import android.database.Cursor;
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
/*Encapsulation in this class should consider enclosing the query for the content resolver,
* there is no point in having the bundle as a param for constructing the query with it,
* such a thing as a completely god class that can be applied to any content resolver is not consistent with the implemented methods,
* look at the methods, the logic of whatever is done is tied to the query,
* bundle should be for passing extra params or for getting params of a previous activity, but not for assembling the query*/
public class CustomLoaderManager implements LoaderManager.LoaderCallbacks<Cursor> {

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("onCreateLoader", "id: " + String.valueOf(id));
        Uri uri = Uri.parse(args.getString(URI));
        String[] projection = args.getStringArray(PROJECTION);
        String selection = args.getString(SELECTION);
        String[] selectionArgs = args.getStringArray(ARGUMENTS);
        String sort = args.getString(SORT);
        return new CursorLoader(activity, uri, projection, selection, selectionArgs, sort);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        /*if something then restart the loader
        loaderManager.restartLoader(CUSTOM_LOADER_ID, null, this);*/
        if (data != null) {
            Log.d("CURSOR", "is not null");
            int size = data.getCount();
            Log.d("CURSOR_ SIZE", String.valueOf(size));
        } else {
            Log.d("CURSOR", "is null");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
