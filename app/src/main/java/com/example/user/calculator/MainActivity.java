package com.example.user.calculator;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.calculator.Fragment.ResultListFragment;
import com.example.user.calculator.SQLite.CalculContract;
import com.example.user.calculator.SQLite.CalculDbHelper;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    static final String STATE_NUMBER = "number"; // 상태복원 키값
    static final String STATE_SIC = "sic";       // 상태복원 키값
    static final String STATE_RESULT = "result";
    static final String STATE_VALUES_NUM1 = "values_num1";
    static final String STATE_VALUES_NUM2 = "values_num2";
    static final String STATE_IS_OPERATION = "isOperation";
    static final String STATE_IS_FULLNUMBER = "isFullNumber";
    static final String STATE_IS_RESULTCOUNT = "isResultCount";
    static final String STATE_CALCULEND = "calculEnd";

    ContentValues contentValues = new ContentValues();


    FrameLayout container;                     // 계산기록 띄울 레이아웃
    Fragment resultListFragment = ResultListFragment.newInstance();     // 계산기록 프래그먼트

    EditText numberEdit;
    TextView sicTextView;

    LinearLayout resultList;
    Button[] buttons = new Button[12];
    Integer[] buttonId = {
            R.id.btn00, R.id.btn01, R.id.btn02, R.id.btn03, R.id.btn04, R.id.btn05,
            R.id.btn06, R.id.btn07, R.id.btn08, R.id.btn09, R.id.btn10, R.id.btn11
    };

    Button[] mathBtn = new Button[4];
    Integer[] mathBtnId = {
            R.id.math01_btn, R.id.math02_btn, R.id.math03_btn, R.id.math04_btn
    };

    Values values;
    DecimalFormat df = new DecimalFormat("#,###.##########");

    int isResultListShow = 0; // 계산기록창을 보였다 안보였다 하기 위한 체크 변수
    int isOperation = 0; // 연산기호 버튼을 연속으로 눌렀는지 판단하기 위한 체크 변수
    int isFullNumber = 0; // 연산을 위한 변수가 준비 됬는지 판단하기 위한 체크 변수
    int isResultCount = 0; //결과 버튼을 여러번 눌렀는지 체크 변수

    String number = "";
    String sic = "";

    double result = 0;

    boolean calculEnd = false;


    // TODO 상태 저장
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_NUMBER, number);
        outState.putString(STATE_SIC, sic);
        outState.putDouble(STATE_RESULT, result);

        outState.putDouble(STATE_VALUES_NUM1, values.getNum1());
        outState.putDouble(STATE_VALUES_NUM2, values.getNum2());

        outState.putInt(STATE_IS_OPERATION, isOperation);
        outState.putInt(STATE_IS_FULLNUMBER, isFullNumber);
        outState.putInt(STATE_IS_RESULTCOUNT, isResultCount);

        outState.putBoolean(STATE_CALCULEND, calculEnd);

        closeFragment();

        super.onSaveInstanceState(outState);
    }

    // TODO 상태 복원 
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        number = savedInstanceState.getString(STATE_NUMBER);
        sic = savedInstanceState.getString(STATE_SIC);
        result = (savedInstanceState.getDouble(STATE_RESULT));

        isOperation = savedInstanceState.getInt(STATE_IS_OPERATION);
        isFullNumber = savedInstanceState.getInt(STATE_IS_FULLNUMBER);
        isResultCount = savedInstanceState.getInt(STATE_IS_RESULTCOUNT);

        calculEnd = savedInstanceState.getBoolean(STATE_CALCULEND);

        values.setNum1(savedInstanceState.getDouble(STATE_VALUES_NUM1));
        values.setNum2(savedInstanceState.getDouble(STATE_VALUES_NUM2));

        numberEdit.setText(number);
        sicTextView.setText(sic);

        if (result != 0) {
            numberEdit.setText(df.format(result));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(); // 뷰 초기화

        /** 00,0~9,'.' 까지 버튼 */
        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (calculEnd) {
                        clear();
                        calculEnd = false;
                    }

                    isResultCount = 0;
                    isOperation = 0;


                    number += buttons[index].getText().toString();

                    numberEdit.setText(inputNumComma(number));

                    if (number.indexOf(".") != -1) {
                        numberEdit.setText(inputNumComma(number));
                        if (number.substring(0, 1).equals(".")) {
                            number = "0.";
                            numberEdit.setText(number);
                        }
                    } else {
                        if (number.substring(0, 1).equals("0")) {
                            number = "";
                            numberEdit.setText(number);
                        }
                    }


                }
            });
        }

        /** 계산기록 ( 보이기 / 가리기 ) 버튼 */
        resultList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isResultListShow++;
                if (isResultListShow == 1) {
                    openFragment();
                } else {
                    closeFragment();

                }

            }
        });
    }

    public void openFragment() { // 프래그먼트를 추가
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.open, R.anim.close, R.anim.open, R.anim.close).
                add(R.id.container_result, resultListFragment).commit();
    }

    public void closeFragment() { // 프래그먼트를 삭제
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.open, R.anim.close, R.anim.open, R.anim.close).
                remove(resultListFragment).commit();
        isResultListShow = 0;
    }

    /*** 뷰 초기화 */
    public void init() {
        resultList = (LinearLayout) findViewById(R.id.resultList_btn);

        container = (FrameLayout) findViewById(R.id.container_result);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = (Button) findViewById(buttonId[i]);
        }

        for (int i = 0; i < mathBtn.length; i++) {
            mathBtn[i] = (Button) findViewById(mathBtnId[i]);
        }

        numberEdit = (EditText) findViewById(R.id.input_edit);
        sicTextView = (TextView) findViewById(R.id.expression_text);

        numberEdit.setInputType(InputType.TYPE_NULL);

        values = new Values();


    }

    /*** 더하기 버튼 */
    public void onClickSum(View view) {
        buttons[11].setClickable(true);
        calculationing("+");
    }

    /*** 뺴기 버튼 */
    public void onClickSub(View view) {
        buttons[11].setClickable(true);
        calculationing("-");
    }

    /*** 곱하기 버튼 */
    public void onClickMul(View view) {
        buttons[11].setClickable(true);
        calculationing("*");
    }

    /*** 나누기 버튼 */
    public void onClickDiv(View view) {
        buttons[11].setClickable(true);
        calculationing("÷");
    }

    /*** 퍼센트 계산 버튼 */
    public void persent_btn(View view) {
        if (values.getNum1() != 0) {
            if (isOperation == 0 && isResultCount == 0 && !numberEdit.getText().toString().equals("")) {
                if (!values.isNumCheck()) {
                    number = numberEdit.getText().toString();
                    double num = Double.parseDouble(number);
                    result = values.getNum1();

                    double end = num / 100 * result;

                    numberEdit.setText(df.format(end));
                    number = String.valueOf(end);
                } else {
                    number = numberEdit.getText().toString();
                    double num = Double.parseDouble(number);
                    result = values.resultCalculation();

                    double end = num / 100 * result;

                    numberEdit.setText(df.format(end));
                    number = String.valueOf(end);
                }
            }
        }
    }

    /*** CE (입력창만 비움) 버튼 */
    public void ce_btn(View view) {
        number = "";
        numberEdit.setText(number);
    }

    /*** C(clear) 버튼 */
    public void clear_btn(View view) {
        clear();
    }

    /*** backspace 버튼 */
    public void backspace_btn(View view) {
        if (!number.equals("")) {
            number = number.substring(0, number.length() - 1);

            if (number.length() > 0) numberEdit.setText(comma(number));
            else {
                buttons[11].setClickable(true);
                numberEdit.setText("");
            }

        } else {
            buttons[11].setClickable(true);
            numberEdit.setText("");
        }

    }

    /*** = 버튼 */
    public void result_btn(View view) {
        isResultCount++;

        // = 버튼을 처음 누르고
        if (isResultCount == 1) {
            // 계산을 위한 변수가 입력이 다 안되 있으면
            if (!values.isNumCheck()) {
                // 결과창이 숫자가 입력 되어 있으면
                if (!number.equals("")) {
                    if (values.getNum1() == 0) {
                        number = numberEdit.getText().toString();
                        sic = number;
                        values.setSic(sic);
                        values.setNum(Double.parseDouble(deleteComma(number)));

                        number = "";
                        sicTextView.setText(sic);
                        numberEdit.setText(df.format(values.getNum1()));

                        calculEnd = true;
                        buttons[11].setClickable(true);

                        isOperation = 0;
                    } else {
                        number = numberEdit.getText().toString();
                        sic += number;
                        values.setSic(sic);
                        values.setNum(Double.parseDouble(deleteComma(number)));

                        result = values.resultCalculation();

                        // TODO DB 에 최종 값 저장------------------------------------------------------------------------
                        contentValues.put(CalculContract.CalculEntry.COLUMN_NAME_SIC, sic);
                        contentValues.put(CalculContract.CalculEntry.COLUMN_NAME_RESULT, df.format(result));

                        SQLiteDatabase db = CalculDbHelper.getInstance(this).getWritableDatabase();
                        long newRowId = db.insert(CalculContract.CalculEntry.TABLE_NAME, null, contentValues);
                        if (newRowId == -1) {
                            Toast.makeText(this, "저장에 문제가 생겼습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        }

                        // TODO ------------------------------------------------------------------------------------------

                        number = "";
                        sicTextView.setText(sic);
                        numberEdit.setText(df.format(result));

                        calculEnd = true;
                        buttons[11].setClickable(true);

                        isOperation = 0;
                    }

                } else {
                    if (!(values.getNum1() == 0)) {
                        number = String.valueOf(values.getNum1());
                        numberEdit.setText(comma(number));
                        sic = sic.substring(0, sic.length() - 1);
                        sicTextView.setText(sic);

                        number = "";

                        calculEnd = true;
                        buttons[11].setClickable(true);
                        isOperation = 0;
                    }
                }
                // 계산을 위한 변수가 다 차 있으면
            } else {
                // 계산식에 끝자리가 기호 라면
                String check = sic.substring(sic.length() - 1, sic.length());
                if (check.equals("+") || check.equals("-") || check.equals("*") || check.equals("/")) {

                    if (isOperation == 0) {
                        sic += number;
                        values.setSic(sic);
                        result = values.resultCalculation();

                        // TODO DB 에 최종 값 저장------------------------------------------------------------------------
                        contentValues.put(CalculContract.CalculEntry.COLUMN_NAME_SIC, sic);
                        contentValues.put(CalculContract.CalculEntry.COLUMN_NAME_RESULT, df.format(result));

                        SQLiteDatabase db = CalculDbHelper.getInstance(this).getWritableDatabase();
                        long newRowId = db.insert(CalculContract.CalculEntry.TABLE_NAME, null, contentValues);
                        if (newRowId == -1) {
                            Toast.makeText(this, "저장에 문제가 생겼습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        }

                        // TODO ------------------------------------------------------------------------------------------

                        number = "";

                        sicTextView.setText(sic);
                        numberEdit.setText(df.format(result));

                        calculEnd = true;
                        buttons[11].setClickable(true);

                        isOperation = 0;

                    } else {
                        sic = sic.substring(0, sic.length() - 1);
                        values.setSic(sic);
                        result = values.resultCalculation();

                        // TODO DB 에 최종 값 저장------------------------------------------------------------------------
                        contentValues.put(CalculContract.CalculEntry.COLUMN_NAME_SIC, sic);
                        contentValues.put(CalculContract.CalculEntry.COLUMN_NAME_RESULT, df.format(result));

                        SQLiteDatabase db = CalculDbHelper.getInstance(this).getWritableDatabase();
                        long newRowId = db.insert(CalculContract.CalculEntry.TABLE_NAME, null, contentValues);
                        if (newRowId == -1) {
                            Toast.makeText(this, "저장에 문제가 생겼습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                        }

                        // TODO ------------------------------------------------------------------------------------------

                        number = "";

                        sicTextView.setText(sic);
                        numberEdit.setText(df.format(result));

                        calculEnd = true;
                        buttons[11].setClickable(true);

                        isOperation = 0;

                    }
                }

            }
        }
    }

    //---------------- 사용자 정의 함수 -----------------------

    /*** 기호에 따른 예외처리 (+,-,*,/) */
    public void calculationing(String giho) {
        isOperation++;

        if (isResultCount == 0) {
            if (isOperation == 1 && !numberEdit.getText().toString().equals("")) {
                sic += comma(number) + giho;
                sicTextView.setText(sic);

                if (sic.substring(0, 1).equals(giho)) {

                    sic = "";

                    sicTextView.setText(sic);

                } else {

                    values.setNum(Double.parseDouble(deleteComma(number)));
                    values.setSic(sic);

                    number = "";
                    numberEdit.setText("");

                    if (values.isNumCheck()) {
                        result = values.resultCalculation();
                        numberEdit.setText(df.format(result));
                    }

                }
            } else {

                if (!sic.equals("")) {
                    sic = sic.substring(0, sic.length() - 1) + giho;

                    values.setSic(sic);

                    sicTextView.setText(sic);
                    number = "";
                    numberEdit.setText("");

                    if (!values.isNumCheck()) {
                        numberEdit.setHint(df.format(values.getNum1()));
                    } else {
                        numberEdit.setHint(df.format(values.resultCalculation()));
                    }
                }

            }

        } else {
            if (calculEnd) {

                if (!values.isNumCheck()) {
                    sic += giho;
                    values.setSic(sic);
                    number = "";
                    sicTextView.setText(sic);
                    numberEdit.setText("");
                    numberEdit.setHint(df.format(values.getNum1()));
                    buttons[11].setClickable(true);

                    isOperation = 0;
                    isResultCount = 0;
                    calculEnd = false;

                } else {

                    result = values.resultCalculation();
                    sic = String.valueOf(df.format(result)) + giho;
                    values.setSic(sic);
                    number = "";
                    sicTextView.setText(sic);
                    numberEdit.setText("");
                    numberEdit.setHint(df.format(result));
                    buttons[11].setClickable(true);

                    isOperation = 0;
                    isResultCount = 0;
                    calculEnd = false;

                }

            }
        }

    }

    /*** 값 초기화 */
    public void clear() {
        // 뷰에 들어갈 변수값 초기화
        number = "";
        sic = "";

        // 뷰 초기화
        numberEdit.setText("");
        sicTextView.setText("");
        numberEdit.setHint("");

        // 체크 변수들
        isResultListShow = 0;
        isOperation = 0;
        isFullNumber = 0;
        isResultCount = 0;

        // 계산을 위한 클래스 초기화
        values.clear();

        buttons[11].setClickable(true);
    }

    /*** 콤마 찍기 */
    public String comma(String str) {
        double aa = Double.parseDouble(str);
        String result = df.format(aa);
        return result;
    }

    /*** 숫자를 입력할 때 콤마 및 . 으로 변환해서 결과 출력 */
    public String inputNumComma(String str) {

        if (str.indexOf(".") != -1) {
            String nonDot = str.substring(0, str.indexOf("."));
            String check = nonDot;

            for (int i = nonDot.length() - 3; i > 0; i -= 3) {
                check = new StringBuilder(check).insert(i, ",").toString();
            }
            str = check + str.substring(str.indexOf("."), str.length());

            return str;
        } else {
            String check = str;

            for (int i = str.length() - 3; i > 0; i -= 3) {
                check = new StringBuilder(check).insert(i, ",").toString();
            }
            str = check;

            return str;
        }
    }

    /*** 콤마 지우기 (values의 num값을 세팅할 때 */
    public String deleteComma(String str) {
        if (str.indexOf(",") != -1) {
            return str.replaceAll(",", "");
        } else {
            return str;
        }
    }

}
