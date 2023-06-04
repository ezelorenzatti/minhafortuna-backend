package br.com.lorenzatti.minhafortuna.backend.shared.converter;

import br.com.lorenzatti.minhafortuna.backend.shared.exception.DataParseException;
import br.com.lorenzatti.minhafortuna.backend.shared.utils.DataUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DataConverter {

    public Date toDate(String toDate) {
        Date date = null;
        try {
            date = DataUtils.toDate(toDate);
        } catch (DataParseException e) {
        }
        return date;
    }

    public Date toDate(String toDate, String pattern) {
        Date date = null;
        try {
            date = DataUtils.toDate(toDate, pattern);
        } catch (DataParseException e) {
        }
        return date;
    }

    public String toString(Date date) {
        return DataUtils.toString(date);
    }

    public String toString(Date date, String pattern) {
        return DataUtils.toString(date, pattern);
    }

}
