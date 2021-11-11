package br.com.lorenzatti.minhafortuna.backend.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private Boolean sucesso;
    private String mensagem;
    private T data;
    private List<String> erros;

}
