package br.com.lorenzatti.minhafortuna.backend.currency.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CurrencyDto {

    private String name;

    private String code;

    private Boolean custom;
}
