package com.example.user.calculator.SQLite;

import android.provider.BaseColumns;

public class CalculContract {
    private CalculContract(){}

    public static class CalculEntry implements BaseColumns {
        public static final String TABLE_NAME = "calculationResult";
        public static final String COLUMN_NAME_SIC = "sic";
        public static final String COLUMN_NAME_RESULT = "result";

    }

}
