package br.com.lorenzatti.minhafortuna.backend.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignUpDto {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String confirmPassword;

}
