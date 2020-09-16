package com.ilegra.desafio.repositories;

import java.util.ArrayList;
import java.util.List;

import com.ilegra.desafio.entity.Client;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class ClientRepository {

    public static final List<Client> clients = new ArrayList<>();

    public static Client searchByCnpj(Long cnpj) {
        return clients.stream()
                .filter(c -> c.getCnpj().equals(cnpj))
                .findFirst()
                .orElse(null);
    }

    public static void clear() {
        clients.clear();
    }
}
