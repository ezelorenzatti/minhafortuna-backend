package br.com.lorenzatti.minhafortuna.backend.plataform.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ExchangeDto {
    private Long id;
    private String name;
    private String url;
}
