package com.ilegra.desafio.repositories;

import java.util.ArrayList;
import java.util.List;

import com.ilegra.desafio.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class ItemRepository {

    public static final List<Item> items = new ArrayList<>();

    public static void clear() {
        items.clear();
    }
}
