package com.appster.dentamatch.util;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class StringUtils {

    public StringUtils() {
    }


    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().length() == 0 || s.equalsIgnoreCase("null");
    }

    public static boolean isNullOrEmpty(ArrayList arraylist) {
        return arraylist == null || arraylist.size() == 0;
    }

    public static boolean isNullOrEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNullOrEmpty(Arrays[] arrays) {
        return arrays == null || arrays.length == 0;
    }

    public static boolean isValidEmail(String s) {
        if (isNullOrEmpty(s)) {
            return false;
        }
        return Pattern.compile("^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$").matcher(s).find();
    }


    public static String trim(String s, String s1) {
        String s2;
        do {
            s2 = s;
            if (!s.endsWith(s1)) {
                break;
            }
            s = s.substring(0, s.length() - s1.length());
        } while (true);
        for (; s2.startsWith(s1); s2 = s2.substring(s1.length(), s2.length())) {
        }
        return s2;
    }


    public static String toCamelCase(String s) {
        String[] parts = s.split(" ");
        String camelCaseString = "";
        for (String part : parts) {
            camelCaseString = camelCaseString + " " + toProperCase(part);
        }
        return camelCaseString.trim();
    }

    static String toProperCase(String s) {
        if (!StringUtils.isNullOrEmpty(s)) {
            return s.substring(0, 1).toUpperCase() +
                    s.substring(1).toLowerCase();
        }
        return "";
    }

    public static String trimFirst(String s) {
        if (!StringUtils.isNullOrEmpty(s) && s.startsWith("/"))
            return s.substring(1);

        return s;
    }

    public static boolean isNullOrEmpty(int[] array) {
        return array == null || array.length == 0;
    }


    public static final <T> List<T> getList(final Class<T[]> clazz, final String json) {
        final T[] jsonToObject = new Gson().fromJson(json, clazz);
        return Arrays.asList(jsonToObject);
    }
}
