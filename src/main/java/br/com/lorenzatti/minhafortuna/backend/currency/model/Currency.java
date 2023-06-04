package br.com.lorenzatti.minhafortuna.backend.currency.model;

import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity()
@Table(name = "TB_CURRENCY")
public class Currency {

    @Id
    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "IS_CUSTOM")
    private Boolean custom;

    @Column(name = "COLOR")
    private String color;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Override
    public String toString() {
        return "Currency{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", custom=" + custom +
                ", color='" + color + '\'' +
                ", user=" + user +
                '}';
    }
}
