package com.ilegra.desafio.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ilegra.desafio.entity.Client;
import com.ilegra.desafio.entity.Item;
import com.ilegra.desafio.entity.Sale;
import com.ilegra.desafio.entity.Salesman;
import com.ilegra.desafio.enums.EventTypeEnum;
import com.ilegra.desafio.exceptions.InvalidLineParametersExceptions;
import com.ilegra.desafio.repositories.ClientRepository;
import com.ilegra.desafio.repositories.SaleRepository;
import com.ilegra.desafio.repositories.SalesmanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LineProcessorService {

    private static final Logger log = LoggerFactory.getLogger(LineProcessorService.class);

    private static final String SEPARATOR = "ç";
    private static final String SEPARATOR_ITEM_GROUP = ",";
    private static final String SEPARATOR_ITEM_FIELDS = "-";

    public void lineProcessor(String line) {
        try {
            if (line == null || line.isEmpty()) return;

            String[] lineArray = line.split(SEPARATOR);
            if (lineArray == null || lineArray.length != 4) {
                log.error("Number of parameters in the line is not recognized");
                return;
            }

            EventTypeEnum type = EventTypeEnum.valueOfType(lineArray[0]);
            switch (type) {
                case SALESMAN:
                    this.salesmanLineProcessor(Arrays.asList(lineArray));
                    break;
                case CLIENT:
                    this.clientLineProcessor(Arrays.asList(lineArray));
                    break;
                case SALE:
                    this.saleLineProcessor(Arrays.asList(lineArray));
                    break;
                default:
                    log.info("Type not found: {}", type);
                    break;
            }
        } catch (NumberFormatException e) {
            log.error("Cannot parse string", e);
        }
    }

    private void salesmanLineProcessor(List<String> lineList) {
        if (lineList == null || lineList.isEmpty() || lineList.size() > 4) {
            throw new InvalidLineParametersExceptions("Salesman line processor invalid parameters");
        }

        Salesman salesman;
        Long cpf = Long.parseLong(lineList.get(1));
        salesman = SalesmanRepository.findByCpf(cpf);

        if (salesman == null) {
            String name = lineList.get(2).trim();
            Float salary = Float.parseFloat(lineList.get(3));
            salesman = new Salesman(cpf, name, salary);
            SalesmanRepository.salesman.add(salesman);
        }
    }

    private void clientLineProcessor(List<String> lineList) {
        if (lineList == null || lineList.isEmpty() || lineList.size() > 4) {
            throw new InvalidLineParametersExceptions("Client line processor invalid parameters");
        }

        Client client;
        Long cnpj = Long.parseLong(lineList.get(1));
        client = ClientRepository.searchByCnpj(cnpj);

        if (client == null) {
            String name = lineList.get(2).trim();
            String businessArea = lineList.get(3).trim();
            client = new Client(cnpj, name, businessArea);
            ClientRepository.clients.add(client);
        }
    }

    private void saleLineProcessor(List<String> lineList) {
        if (lineList == null || lineList.isEmpty() || lineList.size() > 4) {
            throw new InvalidLineParametersExceptions("Client line processor invalid parameters");
        }

        Long id = Long.parseLong(lineList.get(1));
        String itemsString = lineList.get(2);
        String salesmanName = lineList.get(3).toUpperCase();

        List<Item> items = this.parseItems(itemsString);
        Salesman salesman = SalesmanRepository.findByName(salesmanName);

        Sale sale = new Sale(id, items, salesman);
        SaleRepository.addSale(sale);
    }



    private List<Item> parseItems(String itemsString) {
        if (itemsString == null || itemsString.isEmpty()) {
            throw new InvalidLineParametersExceptions("Ivalid items string parameters");
        }

        itemsString = itemsString.replace("[", "").replace("]", "");
        String[] itemsGroup = itemsString.split(SEPARATOR_ITEM_GROUP);
        List<Item> items = new ArrayList<>();

        for (String itemFields : itemsGroup) {
            String[] fields = itemFields.split(SEPARATOR_ITEM_FIELDS);

            if (fields.length == 3) {
                Long id = Long.parseLong(fields[0]);
                Long quantity = Long.parseLong(fields[1]);
                Float price = Float.parseFloat(fields[2]);
                Item item = new Item(id, quantity, price);
                items.add(item);
            } else {
                log.error("Cannot parse item string to object");
            }
        }
        return items;
    }
}
