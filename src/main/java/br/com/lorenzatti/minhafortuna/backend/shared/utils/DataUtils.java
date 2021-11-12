package br.com.lorenzatti.minhafortuna.backend.shared.utils;

import br.com.lorenzatti.minhafortuna.backend.shared.exception.DataParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils {

    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static Date toDate(String toParse) throws DataParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date data = null;
        try {
            data = sdf.parse(toParse);
        } catch (ParseException e) {
            throw new DataParseException(DATE_FORMAT);
        }
        return data;
    }

    public static String toString(Date toString) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String parsed = sdf.format(toString);
        return parsed;
    }
}
