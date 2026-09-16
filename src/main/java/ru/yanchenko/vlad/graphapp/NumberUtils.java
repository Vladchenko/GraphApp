package ru.yanchenko.vlad.graphapp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for checking if strings represent valid numbers.
 * <p>
 * Provides static methods to validate integer and floating-point
 * string representations without throwing exceptions.
 */
public class NumberUtils {
    /**
     * Checks if the given string represents a valid number.
     *
     * @param string the string to check
     * @return true if the string is a valid number, false otherwise
     */
    public static boolean isNumber(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException nfe) {
            Logger.getLogger(NumberUtils.class.getName()).log(Level.WARNING, null, nfe);
            return false;
        }
    }

    /**
     * Checks if the given string represents a valid integer.
     *
     * @param string the string to check
     * @return true if the string is a valid integer, false otherwise
     */
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
