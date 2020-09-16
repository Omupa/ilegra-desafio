package com.ilegra.desafio.entity;

import java.util.List;

import com.ilegra.desafio.enums.EventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    private Long id;
    private List<Item> items;
    private Salesman salesman;

    public Double totalPrice() {
        if (items != null && !items.isEmpty()) {
            return items.stream().parallel()
                    .mapToDouble(Item::getPrice)
                    .sum();
        } else {
            return 0D;
        }
    }
}
