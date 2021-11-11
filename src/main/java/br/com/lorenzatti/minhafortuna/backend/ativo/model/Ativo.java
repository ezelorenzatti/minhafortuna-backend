package br.com.lorenzatti.minhafortuna.backend.ativo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity()
@Table(name = "TB_ATIVO")
public class Ativo {

    @Id
    @Column(name = "SIGLA")
    private String sigla;

    @Column(name = "NOME")
    private String nome;

}
