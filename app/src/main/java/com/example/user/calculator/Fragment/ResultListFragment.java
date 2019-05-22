package com.example.user.calculator.Fragment;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.user.calculator.MainActivity;
import com.example.user.calculator.R;
import com.example.user.calculator.SQLite.CalculAdapter;
import com.example.user.calculator.SQLite.CalculContract;
import com.example.user.calculator.SQLite.CalculDbHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultListFragment extends Fragment {

    public static Fragment newInstance(){
        ResultListFragment fragment = new ResultListFragment();
        return fragment;
    }

    public ResultListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_result_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView);

        // DB 정보 가져오기
        CalculDbHelper dbHelper = CalculDbHelper.getInstance(getContext());
        Cursor cursor = dbHelper.getReadableDatabase().query(CalculContract.CalculEntry.TABLE_NAME,
                        null, null, null, null, null, CalculContract.CalculEntry._ID + " DESC");

        CalculAdapter adapter = new CalculAdapter(getContext(), cursor);
        listView.setAdapter(adapter);

        // 삭제 버튼
        Button button = (Button) view.findViewById(R.id.deleteList);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DB 테이블 내용 삭제 하고 닫기
                SQLiteDatabase db = CalculDbHelper.getInstance(getContext()).getWritableDatabase();
                db.delete(CalculContract.CalculEntry.TABLE_NAME, null, null);
                ((MainActivity)getActivity()).closeFragment();
            }
        });

        return view;
    }
}
