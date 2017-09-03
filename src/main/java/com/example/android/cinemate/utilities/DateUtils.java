package com.example.android.cinemate.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gavin on 03-Sep-17.
 */

public class DateUtils {
    public static String formatDate(String date) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        Date myDate = df.parse(date);
        df = new SimpleDateFormat("dd-mm-yyyy");
        return df.format(myDate);
    }
}
