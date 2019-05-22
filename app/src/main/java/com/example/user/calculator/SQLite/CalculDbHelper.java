package com.example.user.calculator.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalculDbHelper extends SQLiteOpenHelper {
    private static CalculDbHelper sInstance;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "calculator.db";

    private static final String SQL_CREATE_TABLE
            = String.format("CREATE TABLE %s(" +
            "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "%s text, " +
            "%s text)", // SQL 테이블 생성문 끝 // 밑엔 속성
            CalculContract.CalculEntry.TABLE_NAME,
            CalculContract.CalculEntry._ID,
            CalculContract.CalculEntry.COLUMN_NAME_SIC,
            CalculContract.CalculEntry.COLUMN_NAME_RESULT);

    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + CalculContract.CalculEntry.TABLE_NAME ;

    // 펙토리 메서드
    public static synchronized CalculDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CalculDbHelper((context.getApplicationContext()));
        }
        return sInstance;
    }

    public CalculDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION); //db 생성
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
