package br.com.lorenzatti.minhafortuna.backend.operation.dto;

import br.com.lorenzatti.minhafortuna.backend.operation.enums.EnumOperationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class OperationDto {


    private Long id;
    private String date;
    private EnumOperationType operationType;
    private String currencyCode;
    private String currencyName;
    private Integer amount;
    private Double unitValue;
    private Double total;
    private Double taxes;
    private Long plataformId;
    private String plataformName;
    private String color;
    private String lastValueDate;
    private Double lastValue;

}
