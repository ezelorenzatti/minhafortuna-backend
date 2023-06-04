package br.com.lorenzatti.minhafortuna.backend.operation.dto;

import br.com.lorenzatti.minhafortuna.backend.operation.enums.EnumOperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long exchangeId;
    private String exchangeName;
    private String color;
    private String lastValueDate;
    private Double lastValue;

}
