package com.example.user.calculator.SQLite;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.user.calculator.R;

public class CalculAdapter extends CursorAdapter {
    public CalculAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.result_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView sicText = (TextView) view.findViewById(R.id.sic_list_text);
        TextView resultText = (TextView) view.findViewById(R.id.result_list_text);

        sicText.setText(cursor.getString(cursor.getColumnIndexOrThrow(CalculContract.CalculEntry.COLUMN_NAME_SIC)));
        resultText.setText("= "+cursor.getString(cursor.getColumnIndexOrThrow(CalculContract.CalculEntry.COLUMN_NAME_RESULT)));

    }
}
