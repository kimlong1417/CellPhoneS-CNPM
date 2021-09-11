package utils;

import java.text.DecimalFormat;

public class NumberHelper {

    public static String addComma(String number) {
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }

    public static String removeComma(String number) {
        return number.replaceAll("[^\\d.]", "");
    }

    public static Boolean isNumber(String input) {
        return input.matches("\\d*");
    }
}
