package cl.cutiko.contentproviders;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.UserDictionary;
import android.util.Log;

import java.util.Locale;

public class DictionaryService extends IntentService {

    private static final String ACTION_DICTIONARY = "cl.cutiko.contentproviders.action.ACTION_DICTIONARY";
    private static final String WORD = "cl.cutiko.contentproviders.extra.WORD";



    public DictionaryService() {
        super("DictionaryService");
    }

    public static void startDictionary(Context context, String searchedWord) {
        Intent intent = new Intent(context, DictionaryService.class);
        intent.putExtra(WORD, searchedWord);
        intent.setAction(ACTION_DICTIONARY);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DICTIONARY.equals(action)) {
                final String word = intent.getStringExtra(DictionaryService.WORD);
                searchValidation(word);
            }
        }
    }

    private void searchValidation(String word) {
        if (word.trim().length() > 0) {
            dictionarySearch(word);
        }
    }

    private void dictionarySearch(String word){

        /* Example for getting a single uri
        * Uri singleUri = ContentUris.withAppendedId(UserDictionary.Words.CONTENT_URI,4);*/

        /*This is how the selectionArgs work
        * This is a very quick explanation http://stackoverflow.com/a/20777605/4017501 below original doc
        * La expresión que especifica las filas para recuperar se divide en una cláusula de selección y argumentos de selección.
        * La cláusula de selección es una combinación de expresiones lógicas y booleanas, nombres de columnas y valores (la variable mSelectionClause).
        * Si especificas el parámetro reemplazable ? en lugar de un valor,
        * el método de consulta obtiene el valor de la matriz de argumentos de selección (la variable mSelectionArgs).
        * */

        String[] projection = {UserDictionary.Words._ID, UserDictionary.Words.WORD, UserDictionary.Words.LOCALE};
        String selection = UserDictionary.Words.WORD + " LIKE ?";
        String[] selectionArgs = {"%%%"+word+"%%%"};
        String sort = UserDictionary.Words._ID + " ASC";

        Cursor cursor = getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sort
        );


        cursorIterator(cursor);
        insertRow(cursor, word);
    }

    private void insertRow(Cursor cursor, String word){
        if (cursor == null) {
            Log.d("CURSOR", "is null");
        } else {
            if (cursor.getCount() > 0) {
                Log.d("CURSOR_SIZE", String.valueOf(cursor.getCount()));
                updateRows(word);
            } else {
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(UserDictionary.Words.APP_ID, "example.user");
                mNewValues.put(UserDictionary.Words.LOCALE, Locale.getDefault().toString());
                mNewValues.put(UserDictionary.Words.WORD, word);
                mNewValues.put(UserDictionary.Words.FREQUENCY, "100");

                Uri newUri = getContentResolver().insert(
                        UserDictionary.Words.CONTENT_URI,
                        mNewValues
                );

                Log.d("INSERTION", newUri.toString());

                /* The new Uri can be parse like this
                *  ContentUris.parseId() */
            }
            cursor.close();
        }
    }

    private void updateRow(Uri uri){
        long contentValues = ContentUris.parseId(uri);
    }

    private void updateRows(String word){
        String selection = UserDictionary.Words.WORD + " LIKE ?";
        String[] selectionArgs = {"%%%"+word+"%%%"};

        ContentValues contentValues = new ContentValues();
        contentValues.putNull(UserDictionary.Words.FREQUENCY);
        contentValues.put(UserDictionary.Words.LOCALE, "fake_Locale");
        int updateCount = getContentResolver().update(
                UserDictionary.Words.CONTENT_URI,
                contentValues,
                selection,
                selectionArgs
        );

        Log.d("UPDATE_COUNT", String.valueOf(updateCount));
    }

    private void updateByCursor(){

    }

    private void cursorIterator(Cursor cursor){
        //Very elegant, such a loop, much while, wow, doge
        if (cursor != null) {
            int index = cursor.getColumnIndex(UserDictionary.Words.LOCALE);
            //int index = cursor.getColumnIndex(UserDictionary.Words.WORD);
            Log.d("INDEX", String.valueOf(index));
            while (cursor.moveToNext()) {
                //This is how getType work
                int type = cursor.getType(index);
                switch (type) {
                    case Cursor.FIELD_TYPE_STRING:
                        String word = cursor.getString(index);
                        Log.d("WORD", word + " " + type);
                        break;
                    default:
                        Log.d("TYPE", "another type");
                        break;
                }
            }
        } else {
            Log.d("CURSOR", "is null");
        }
    }

}
