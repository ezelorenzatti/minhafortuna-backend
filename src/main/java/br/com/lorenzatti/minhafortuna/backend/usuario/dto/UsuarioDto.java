package br.com.lorenzatti.minhafortuna.backend.usuario.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class UsuarioDto implements Serializable {

    private Long id;

    private String senha;

    private String nome;

    private String email;

    private String fone;

    private String confirmarSenha;
}

