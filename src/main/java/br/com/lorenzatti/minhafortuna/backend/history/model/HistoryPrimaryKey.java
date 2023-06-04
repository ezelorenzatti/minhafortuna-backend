package br.com.lorenzatti.minhafortuna.backend.history.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public class HistoryPrimaryKey implements Serializable {

    private String code;
    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryPrimaryKey that = (HistoryPrimaryKey) o;
        return Objects.equals(code, that.code) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, date);
    }
}
