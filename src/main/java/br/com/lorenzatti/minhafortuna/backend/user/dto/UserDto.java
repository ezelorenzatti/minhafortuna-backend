package br.com.lorenzatti.minhafortuna.backend.user.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class UserDto implements Serializable {

    private Long id;

    private String password;

    private String name;

    private String email;

    private String phone;

    private String confirmPassword;
}

