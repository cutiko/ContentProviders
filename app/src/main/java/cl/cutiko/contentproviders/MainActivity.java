package cl.cutiko.contentproviders;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int EXAMPLE_LOADER_ID = 323;
    private EditText searchEt;
    private RecyclerView recyclerView;
    private DictionaryCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchEt = (EditText) findViewById(R.id.searchEt);
        recyclerView = (RecyclerView) findViewById(R.id.dictionaryRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter = new DictionaryCursorAdapter(null);
        setFab();


    }

    private void setFab(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DictionaryService.startDictionary(MainActivity.this, searchEt.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putString(DictionaryLoader.URI, String.valueOf(UserDictionary.Words.CONTENT_URI));
                bundle.putStringArray(DictionaryLoader.PROJECTION, new String[]{UserDictionary.Words._ID, UserDictionary.Words.WORD, UserDictionary.Words.LOCALE});
                bundle.putString(DictionaryLoader.SELECTION, UserDictionary.Words.WORD + " LIKE ?");
                bundle.putStringArray(DictionaryLoader.ARGUMENTS, new String[]{"%%%"+searchEt.getText().toString()+"%%%"});
                bundle.putString(DictionaryLoader.SORT, UserDictionary.Words._ID + " ASC");
                //new DictionaryLoader(MainActivity.this, bundle);
                new MainDictionaryLoader(MainActivity.this, bundle);
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //DictionaryService.startDelete(MainActivity.this, searchEt.getText().toString());
                return true;
            }
        });
    }

    private class MainDictionaryLoader extends DictionaryLoader {

        public MainDictionaryLoader(AppCompatActivity activity, Bundle args) {
            super(activity, args);
        }


        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            super.onLoadFinished(loader, data);
            adapter = new DictionaryCursorAdapter(data);
            recyclerView.setAdapter(adapter);
        }
    }
}
