package com.ilegra.desafio.entity;

import com.ilegra.desafio.enums.EventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    private Long cnpj;
    private String name;
    private String businessArea;
}
