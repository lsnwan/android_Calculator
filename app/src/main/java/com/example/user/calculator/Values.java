package com.example.user.calculator;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Values {
    private String sic = "";
    private double result, num1, num2;

    private StringTokenizer st;
    ;
    private ArrayList<String> numberList = new ArrayList<>();


    public Values() {
    }

    public void setSic(String sic) {
        this.sic = sic;
    }

    public void setNum(double number) {
        if (num1 == 0) {
            num1 = number;
        } else {
            num2 = number;
        }
    }

    public double getNum1() {
        return num1;
    }

    /**
     * 값이 두개 변수에 모두 들어갔는지 체크
     */
    public boolean isNumCheck() {
        if (num1 == 0 || num2 == 0) {
            return false;
        } else {
            return true;
        }
    }

    public String getSic() {
        return sic;
    }

    public double getNum2() {
        return num2;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public void setNum1(double num1) {
        this.num1 = num1;
    }

    public void setNum2(double num2) {
        this.num2 = num2;
    }

    /**
     * =을 눌렀을 때 최종 적힌 숫자랑 계산하는 함수
     */
    public double resultCalculation() {

        sic = sic.replaceAll(",", "");
        st = new StringTokenizer(sic, "+-*÷", true);
        numberList.clear();

        while (st.hasMoreTokens()) {
            numberList.add(st.nextToken());
        }

        for (int i = 0; i < numberList.size() - 2; i += 2) {

            if (i == 0) {
                num1 = Double.parseDouble(numberList.get(i));
                num2 = Double.parseDouble(numberList.get(i + 2));

                String giho = numberList.get(i + 1);
                switch (giho) {
                    case "+":
                        result = num1 + num2;
                        break;

                    case "-":
                        result = num1 - num2;
                        break;

                    case "*":
                        result = num1 * num2;
                        break;

                    case "÷":
                        result = num1 / num2;
                        break;
                }
            } else {
                num2 = Double.parseDouble(numberList.get(i + 2));
                String giho = numberList.get(i + 1);
                switch (giho) {
                    case "+":
                        result = result + num2;
                        break;

                    case "-":
                        result = result - num2;
                        break;

                    case "*":
                        result = result * num2;
                        break;

                    case "÷":
                        result = result / num2;
                        break;
                }
            }
        }

        return result;
    }

    /**
     * 초기화
     */
    public void clear() {
        sic = "";
        result = num1 = num2 = 0;
        numberList.clear();
    }
}
