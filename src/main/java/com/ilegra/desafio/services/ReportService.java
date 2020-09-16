package com.ilegra.desafio.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.google.gson.Gson;
import com.ilegra.desafio.entity.Report;
import com.ilegra.desafio.entity.Sale;
import com.ilegra.desafio.entity.Salesman;
import com.ilegra.desafio.repositories.ClientRepository;
import com.ilegra.desafio.repositories.SaleRepository;
import com.ilegra.desafio.repositories.SalesmanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    public static String OUTPUT = System.getProperty("desafio.outputdir");

    private final SaleRepository saleRepository;

    public ReportService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public void generateReport (String fileName) {
        Objects.requireNonNull(OUTPUT, "Output path can not be null");

        int clients = ClientRepository.clients.size();
        int salesman = SalesmanRepository.salesman.size();
        Sale mostExpensiveSale = SaleRepository.mostExpensiveSale;
        Salesman worstSalesman = SalesmanRepository.worstSalesman();

        Report report = new Report((long) clients, (long) salesman, mostExpensiveSale.getId(), worstSalesman);
        this.saveReport(report, fileName);
    }

    private void saveReport(Report report, String fileName) {
        try {
            String json =  new Gson().toJson(report);
            Path path = Paths.get(OUTPUT, fileName);
            Files.write(path, json.getBytes());
        } catch (IOException e) {
            log.error("Error on save report to output file", e);
        }
    }
}
