package ru.yanchenko.vlad.graphapp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NumberUtils {
    public static boolean isNumber(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException nfe) {
            Logger.getLogger(NumberUtils.class.getName()).log(Level.WARNING, null, nfe);
            return false;
        }
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException nfe) {
            Logger.getLogger(NumberUtils.class.getName()).log(Level.WARNING, null, nfe);
            return false;
        }
    }
}
