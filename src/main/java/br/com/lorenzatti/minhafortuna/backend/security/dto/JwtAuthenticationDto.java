package br.com.lorenzatti.minhafortuna.backend.security.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationDto {

    private String email;
    private String password;
    private Boolean simulateData = Boolean.FALSE;

}
