package br.com.lorenzatti.minhafortuna.backend.historico.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "TB_HISTORICO")
public class Historico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SIGLA")
    private String sigla;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "DATA")
    private Date data;

    @Column(name = "COMPRA")
    private Double compra;

    @Column(name = "VENDA")
    private Double venda;

}
