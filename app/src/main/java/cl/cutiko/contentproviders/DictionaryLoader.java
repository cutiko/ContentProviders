package cl.cutiko.contentproviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.Locale;

/**
 * Created by cutiko on 08-03-17.
 */
/*Encapsulation in this class should consider enclosing the query for the content resolver,
* there is no point in having the bundle as a param for constructing the query with it,
* such a thing as a completely god class that can be applied to any content resolver is not consistent with the implemented methods,
* look at the methods, the logic of whatever is done is tied to the query,
* bundle should be for passing extra params or for getting params of a previous activity, but not for assembling the query*/
public class DictionaryLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private String word;
    private final LoaderManager loaderManager;
    private final Context context;
    private static final int CUSTOM_LOADER_ID = 343;
    private CursorLoader cursorLoader;

    public DictionaryLoader(String word, LoaderManager loaderManager, Context context) {
        this.word = word;
        this.loaderManager = loaderManager;
        this.context = context;
        loaderManager.initLoader(CUSTOM_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("onCreateLoader", "id: " + String.valueOf(id));
        Uri uri = UserDictionary.Words.CONTENT_URI;
        String[] projection = {UserDictionary.Words._ID, UserDictionary.Words.WORD, UserDictionary.Words.LOCALE};
        String selection = UserDictionary.Words.WORD + " LIKE ?";
        String[] selectionArgs = {"%%%"+word+"%%%"};
        String sort = UserDictionary.Words._ID + " ASC";
        cursorLoader = new CursorLoader(context, uri, projection, selection, selectionArgs, sort);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        /*if something then restart the loader
        loaderManager.restartLoader(CUSTOM_LOADER_ID, null, this);*/
        if (data != null) {
            Log.d("CURSOR", "is not null");
            int size = data.getCount();
            Log.d("CURSOR_ SIZE", String.valueOf(size));
            if (size == 0) {
                insert();
            }
        } else {
            Log.d("CURSOR", "is null");
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("onLoaderReset", "event");
    }
    private void insert(){
        ContentValues mNewValues = new ContentValues();
        mNewValues.put(UserDictionary.Words.APP_ID, "example.user");
        mNewValues.put(UserDictionary.Words.LOCALE, Locale.getDefault().toString());
        mNewValues.put(UserDictionary.Words.WORD, word);
        mNewValues.put(UserDictionary.Words.FREQUENCY, "100");

        Uri newUri = context.getContentResolver().insert(
                UserDictionary.Words.CONTENT_URI,
                mNewValues
        );

        Log.d("INSERT", newUri.getPath());
    }

    public void update(String word) {
        this.word = word;
        loaderManager.restartLoader(CUSTOM_LOADER_ID, null, this);
    }


}
