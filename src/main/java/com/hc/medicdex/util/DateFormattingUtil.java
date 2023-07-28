package com.hc.medicdex.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormattingUtil {
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    public Date getDateFromString(String strDate) throws ParseException {
        return dateFormatter.parse(strDate);
    }
    public Date getCurrentDate() throws ParseException {
        Date date = new Date();
        return dateFormatter.parse(dateFormatter.format(date));
    }
    public Date getCurrentDateTime() throws ParseException {
        Date date = new Date();
        return dateTimeFormatter.parse(dateTimeFormatter.format(date));
    }
}
