package br.com.lorenzatti.minhafortuna.backend.shared.utils;

import br.com.lorenzatti.minhafortuna.backend.shared.exception.DataParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static String DATE_FORMAT = "yyyy-MM-dd";

    public static Date toDate(String toParse) throws DataParseException {
        return Utils.toDate(toParse, DATE_FORMAT);
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

    public static String toString(Date toString) {
        return Utils.toString(toString, DATE_FORMAT);
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


    public static String selectRandomString(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            throw new IllegalArgumentException("A lista de strings não pode ser nula ou vazia.");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(stringList.size());

        return stringList.get(randomIndex);
    }

    public static int generateRandomNumber(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("O valor mínimo deve ser menor ou igual ao valor máximo.");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static double calculatePercentage(double originalValue, double percentage) {
        double result = (originalValue * percentage) / 100;
        return result;
    }

    public static Integer sortNumber(List<Integer> numbers) {
        Random random = new Random();
        Integer sortedIndex = random.nextInt(numbers.size());
        Integer sortedNumber = numbers.get(sortedIndex);
        return sortedNumber;
    }

    public static Date getDayBefore() {
        return Utils.getDayBefore(null);
    }

    public static Date getDayBefore(Date date) {
        Date current = new Date();
        if (date != null) {
            current = date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.DATE, -1);
        current = calendar.getTime();
        return current;
    }
}
