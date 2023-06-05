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
    private String color;
    private Boolean allowChange;
    private Boolean isUsed;
}
