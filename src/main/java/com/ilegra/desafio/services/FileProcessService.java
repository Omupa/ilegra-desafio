package com.ilegra.desafio.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.stream.Stream;

import com.ilegra.desafio.repositories.ClientRepository;
import com.ilegra.desafio.repositories.FilesRepository;
import com.ilegra.desafio.repositories.ItemRepository;
import com.ilegra.desafio.repositories.SaleRepository;
import com.ilegra.desafio.repositories.SalesmanRepository;
import com.ilegra.desafio.startup.AppStartup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileProcessService {

    private static final Logger log = LoggerFactory.getLogger(FileProcessService.class);

    private final LineProcessorService lineProcessorService;
    private final ReportService reportService;

    public FileProcessService(LineProcessorService lineProcessorService, ReportService reportService) {
        this.lineProcessorService = lineProcessorService;
        this.reportService = reportService;
    }

    public void processFileList() {
        this.processFiles(FilesRepository.filesToProcess);
    }

    public void processTryAgainFiles() {
        this.processFiles(FilesRepository.tryAgain);
    }

    private void processFiles(Queue<String> queue) {
        if (queue.isEmpty()) return;

        String fileName = queue.peek();
        this.readFile(fileName);
        queue.poll();

        this.resetRepositories();
        this.processFileList();
    }

    private void readFile(String fileName) {
        Path path = Paths.get(AppStartup.INPUT, fileName);

        if (!Files.isReadable(path)) {
            this.insertOnTyAgainQueue(fileName);
            return;
        }

        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(lineProcessorService::lineProcessor);
            reportService.generateReport(fileName);

        } catch (IOException e) {
            log.error("Error on read file {}", fileName, e);
            FilesRepository.failedFiles.add(fileName);
        }
    }

    private void insertOnTyAgainQueue(String fileName) {
        if (!FilesRepository.tryAgain.contains(fileName)) {
            FilesRepository.tryAgain.add(fileName);
            log.info("INSERT ON TRY AGAIN: {}", fileName);
        }
    }

    private void resetRepositories() {
        ClientRepository.clear();
        ItemRepository.clear();
        SaleRepository.clear();
        SalesmanRepository.clear();
    }


}
