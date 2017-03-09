package cl.cutiko.contentproviders;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by cutiko on 09-03-17.
 */

public class DictionaryCursorAdapter extends RecyclerView.Adapter<DictionaryCursorAdapter.ViewHolder> {

    private final Cursor cursor;

    public DictionaryCursorAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String word = cursor.getString(1);
        String locale = cursor.getString(2);
        holder.setView(word, locale);
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView word, locale;

        public ViewHolder(View itemView) {
            super(itemView);
            word = (TextView) itemView.findViewById(android.R.id.text1);
            locale = (TextView) itemView.findViewById(android.R.id.text2);
        }

        public void setView(String word, String locale) {
            this.word.setText(word);
            this.locale.setText(locale);
        }
    }

}
