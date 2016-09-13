package com.squalala.dz6android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Back Packer
 * Date : 29/09/15
 */
public class DateUtils {

    public static String getRelativeTime(Date date) {

        long now = System.currentTimeMillis();

        return (String)
                android.text.format
                        .DateUtils.getRelativeTimeSpanString(date.getTime(), now, android.text.format.DateUtils.FORMAT_ABBREV_ALL);
    }

    public static Date strToDate(String dateStr) {

        Date date = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {

            date = format.parse(dateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


}
