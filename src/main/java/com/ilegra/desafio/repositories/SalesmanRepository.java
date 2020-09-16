package com.ilegra.desafio.repositories;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ilegra.desafio.entity.Sale;
import com.ilegra.desafio.entity.Salesman;
import com.ilegra.desafio.exceptions.MultipleRegisterFindException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class SalesmanRepository {

    public static final List<Salesman> salesman = new ArrayList<>();

    public static Salesman findByCpf(Long cpf) {
        return salesman.stream()
                .filter(s -> s.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }

    public static Salesman findByName(String name) {
        List<Salesman> salesmans = salesman.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        if (salesmans.size() == 1) {
            return salesmans.get(0);
        } else {
            throw new MultipleRegisterFindException("Many or not one salesman finded by name");
        }
    }

    public static Salesman worstSalesman() {
        if (SaleRepository.sales.isEmpty()) return new Salesman();

        Map<Salesman, List<Sale>> map = SaleRepository.sales.stream().collect(Collectors.groupingBy(Sale::getSalesman));
        Map.Entry<Salesman, Long> worstSales = null;

        for (Map.Entry<Salesman, List<Sale>> entry : map.entrySet()) {
            long totalSales = entry.getValue().stream().count();

            if (worstSales == null || totalSales < worstSales.getValue()) {
                worstSales = new AbstractMap.SimpleEntry<>(entry.getKey(), totalSales);
            }
        }
        return worstSales.getKey();
    }

    public static void clear() {
        salesman.clear();
    }
}
