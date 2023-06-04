package br.com.lorenzatti.minhafortuna.backend.shared.utils;

import br.com.lorenzatti.minhafortuna.backend.shared.exception.DataParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class DataUtils {

    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static Date toDate(String toParse) throws DataParseException{
        return DataUtils.toDate(toParse, DATE_FORMAT);
    }

    public static Date toDate(String toParse, String pattern) throws DataParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date data = null;
        try {
            data = sdf.parse(toParse);
        } catch (ParseException e) {
            throw new DataParseException(pattern);
        }
        return data;
    }

    public static String toString(Date toString){
        return DataUtils.toString(toString, DATE_FORMAT);
    }

    public static String toString(Date toString, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String parsed = sdf.format(toString);
        return parsed;
    }


    public static Date getRandomDate(Date start, Date end) {
        LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        long randomDay = ThreadLocalRandom.current().nextLong(daysBetween + 1);
        LocalDate randomDate = startDate.plusDays(randomDay);
        return Date.from(randomDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
