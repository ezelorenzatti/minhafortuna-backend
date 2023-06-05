package br.com.lorenzatti.minhafortuna.backend.plataform.model;

import br.com.lorenzatti.minhafortuna.backend.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Entity()
@Table(name = "TB_EXCHANGE")
public class Exchange implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "URL")
    private String url;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Override
    public String toString() {
        return "Exchange{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", user=" + user +
                '}';
    }
}
