package cl.cutiko.contentproviders;

import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int EXAMPLE_LOADER_ID = 323;
    private EditText searchEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchEt = (EditText) findViewById(R.id.searchEt);
        setFab();
    }

    private void setFab(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DictionaryService.startDictionary(MainActivity.this, searchEt.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putString(CustomLoaderManager.URI, String.valueOf(UserDictionary.Words.CONTENT_URI));
                bundle.putStringArray(CustomLoaderManager.PROJECTION, new String[]{UserDictionary.Words._ID, UserDictionary.Words.WORD, UserDictionary.Words.LOCALE});
                bundle.putString(CustomLoaderManager.SELECTION, UserDictionary.Words.WORD + " LIKE ?");
                bundle.putStringArray(CustomLoaderManager.ARGUMENTS, new String[]{"%%%"+searchEt.getText().toString()+"%%%"});
                bundle.putString(CustomLoaderManager.SORT, UserDictionary.Words._ID + " ASC");
                new CustomLoaderManager(MainActivity.this, bundle);
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

}
