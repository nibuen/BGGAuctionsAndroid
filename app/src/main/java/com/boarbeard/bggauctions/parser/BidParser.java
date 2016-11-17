package com.boarbeard.bggauctions.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leif on 11/16/2016.
 */

public class BidParser {
    public static final String regexAmountMatch = "\\$?([0-9]+[\\.]?[0-9]*)";
    public static final Pattern regexAmountMatchPattern = Pattern.compile(regexAmountMatch);

    public static final Pattern binRegex = Pattern.compile("(B|b)(I|i)(N|n)");

    public static final String digitMatch = "^\\d+$";
    public static final Pattern regexDigitMatchPattern = Pattern.compile(digitMatch);

    public static String parse(String text) {
        Matcher matcher = regexAmountMatchPattern.matcher(text);
        if(matcher.find()) {
            String matched = matcher.group();

            // found a zip code instead try next
            if(matched.length() >= 5 && regexDigitMatchPattern.matcher(matched).find())  {
                if(matcher.find()) {
                    matched = matcher.group();
                }
            }

            return matched;
        }

        return "0";
    }
}
