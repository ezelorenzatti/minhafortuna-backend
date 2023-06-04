package br.com.lorenzatti.minhafortuna.backend.shared.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private Boolean success;
    private String message;
    private T data;
    private String error;

}
