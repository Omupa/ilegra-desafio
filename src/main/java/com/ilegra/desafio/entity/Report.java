package com.ilegra.desafio.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private Long numberOfClients;
    private Long numberOfSalesman;
    private Long mostExpensiveSaleId;
    private Salesman worseSalesman;
}
