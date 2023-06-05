package br.com.lorenzatti.minhafortuna.backend.operation.model;

import br.com.lorenzatti.minhafortuna.backend.currency.model.Currency;
import br.com.lorenzatti.minhafortuna.backend.operation.enums.EnumOperationType;
import br.com.lorenzatti.minhafortuna.backend.plataform.model.Exchange;
import br.com.lorenzatti.minhafortuna.backend.user.model.User;
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
@Table(name = "TB_OPERATION")
public class Operation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATE")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPERATION_TYPE")
    private EnumOperationType operationType;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Currency.class)
    @JoinColumn(name = "CURRENCY_ID")
    private Currency currency;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "UNIT_VALUE")
    private Double unitValue;

    @Column(name = "TOTAL")
    private Double total;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Exchange.class)
    @JoinColumn(name = "EXCHANGE_ID")
    private Exchange exchange;

}
