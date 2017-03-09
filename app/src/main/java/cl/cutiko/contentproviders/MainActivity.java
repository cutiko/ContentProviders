package cl.cutiko.contentproviders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText searchEt;
    private RecyclerView recyclerView;
    private DictionaryCursorAdapter adapter;
    private MainDictionaryLoader mainDictionaryLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchEt = (EditText) findViewById(R.id.searchEt);
        recyclerView = (RecyclerView) findViewById(R.id.dictionaryRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DictionaryCursorAdapter();
        recyclerView.setAdapter(adapter);
        setFab();


    }

    private void setFab(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DictionaryService.startDictionary(MainActivity.this, searchEt.getText().toString());
                //new DictionaryLoader(MainActivity.this, bundle);
                String word = searchEt.getText().toString();
                if (mainDictionaryLoader == null) {
                    mainDictionaryLoader = new MainDictionaryLoader(word, getSupportLoaderManager(), MainActivity.this);
                } else {
                    mainDictionaryLoader.update(word);
                }

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


        public MainDictionaryLoader(String word, LoaderManager loaderManager, Context context) {
            super(word, loaderManager, context);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            super.onLoadFinished(loader, data);
            adapter.addCursor(data);
        }
    }
}
