package br.com.lorenzatti.minhafortuna.backend.bcbapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BCBCurrency {

    @JsonProperty("simbolo")
    private String code;

    @JsonProperty("nomeFormatado")
    private String name;

}
