package com.ilegra.desafio.repositories;

import java.util.ArrayList;
import java.util.List;

import com.ilegra.desafio.entity.Item;
import com.ilegra.desafio.entity.Sale;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class SaleRepository {

    public static final List<Sale> sales = new ArrayList<>();
    public static Sale mostExpensiveSale;

    public static void addSale(Sale sale) {
        Sale exists = sales.parallelStream()
                .filter(s -> s.getId().equals(sale.getId()))
                .findFirst()
                .orElse(null);

        if (exists == null) {
            mostExpensiveSale(sale);
            sales.add(sale);
        }
    }

    private static void mostExpensiveSale(Sale sale) {
        if (SaleRepository.mostExpensiveSale == null)
            SaleRepository.mostExpensiveSale = sale;

        Double totalPrice = sumTotalPrice(sale.getItems());
        Double mostExpensivePrice = sumTotalPrice(SaleRepository.mostExpensiveSale.getItems());

        if (mostExpensivePrice < totalPrice) {
            SaleRepository.mostExpensiveSale = sale;
        }
    }

    private static Double sumTotalPrice(List<Item> items) {
        if (items == null || items.isEmpty()) return 0D;

        return items.stream().mapToDouble(Item::getPrice).sum();
    }

    public static void clear() {
        sales.clear();
    }
}
