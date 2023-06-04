package br.com.lorenzatti.minhafortuna.backend.history.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity()
@Table(name = "TB_HISTORY")
@IdClass(HistoryPrimaryKey.class)
public class History implements Serializable {

    @Id
    @Column(name = "CODE")
    private String code;

    @Id
    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "BUY")
    private Double buy;

    @Column(name = "SELL")
    private Double sell;

}
