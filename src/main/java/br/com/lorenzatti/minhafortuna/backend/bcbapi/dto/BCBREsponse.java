package br.com.lorenzatti.minhafortuna.backend.bcbapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class BCBREsponse {

    @JsonProperty("@odata.context")
    private String context;

    private List<BCBCurrency> value;


}
