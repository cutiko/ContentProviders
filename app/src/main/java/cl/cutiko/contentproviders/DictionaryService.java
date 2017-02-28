package cl.cutiko.contentproviders;

import android.app.IntentService;
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

        if (cursor == null) {
            Log.d("CURSOR", "is null");
        } else {
            if (cursor.getCount() > 0) {
                Log.d("CURSOR", String.valueOf(cursor.getCount()));
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
            }
            cursor.close();
        }


        /* Example for getting a single uri
        * Uri singleUri = ContentUris.withAppendedId(UserDictionary.Words.CONTENT_URI,4);*/

        /*This is how the selectionArgs work
        * This is a very quick explanation http://stackoverflow.com/a/20777605/4017501 below original doc
        * La expresión que especifica las filas para recuperar se divide en una cláusula de selección y argumentos de selección.
        * La cláusula de selección es una combinación de expresiones lógicas y booleanas, nombres de columnas y valores (la variable mSelectionClause).
        * Si especificas el parámetro reemplazable ? en lugar de un valor,
        * el método de consulta obtiene el valor de la matriz de argumentos de selección (la variable mSelectionArgs).
        * */
    }

}
