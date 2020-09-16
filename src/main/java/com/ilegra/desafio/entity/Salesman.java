package com.ilegra.desafio.entity;

import com.ilegra.desafio.enums.EventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Salesman {

    private Long cpf;
    private String name;
    private Float salary;

    public Salesman(Long cpf, String name, Float salary) {
        this.cpf = cpf;
        this.name = name;
        this.salary = salary;
    }
}
