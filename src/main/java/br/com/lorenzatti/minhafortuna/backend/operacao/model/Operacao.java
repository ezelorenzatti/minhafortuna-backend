package br.com.lorenzatti.minhafortuna.backend.operacao.model;

import br.com.lorenzatti.minhafortuna.backend.usuario.model.Usuario;
import br.com.lorenzatti.minhafortuna.backend.ativo.model.Ativo;
import br.com.lorenzatti.minhafortuna.backend.operacao.enums.EnumTipoOperacao;
import br.com.lorenzatti.minhafortuna.backend.plataforma.model.Plataforma;
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
@Table(name = "TB_OPERACAO")
public class Operacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATA", columnDefinition = "")
    private Date data;

    @Enumerated(EnumType.STRING)
    @Column(name = "OPERACAO")
    private EnumTipoOperacao operacao;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Ativo.class)
    @JoinColumn(name = "ATIVO_ID")
    private Ativo ativo;

    @Column(name = "QUANTIDADE")
    private Double quantidade;

    @Column(name = "VALOR_TOTAL")
    private Double valor;

    @Column(name = "TAXAS")
    private Double taxas;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Usuario.class)
    @JoinColumn(name = "USUARIO_ID")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Plataforma.class)
    @JoinColumn(name = "PLATAFORMA_ID")
    private Plataforma plataforma;

}
