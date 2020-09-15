package com.ilegra.desafio.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.ilegra.desafio.startup.AppStartup;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileProcessService {

    private static final Logger log = LoggerFactory.getLogger(FileProcessService.class);

    public static boolean processando = false;
    public static final Queue<String> filesToProcess = new LinkedList<>();
    public static final Queue<String> processedFiles = new LinkedList<>();
    public static final Queue<String> failedFiles = new LinkedList<>();

    public void processFileList() {
        if (filesToProcess.isEmpty()) {
            processando = false;
            return;
        }

        processando = true;
        String fileName = filesToProcess.peek();

        try {
            this.readFile(fileName);
            processedFiles.add(fileName);
        } catch (Exception e) {
            log.error("Erro ao processar arquivo: {}", fileName, e);
            failedFiles.add(fileName);
        }

        filesToProcess.poll();
        this.processFileList();
    }

    private void readFile(String fileName) {
        String filePath = AppStartup.INPUT + fileName;
        File file = new File(filePath);

        if (file.exists() && file.canRead()) {
            log.info("PROCESSANDO O ARQUIVO: {}", fileName);
            try (LineIterator lineIterator = FileUtils.lineIterator(file, "UTF-8")) {

                while (lineIterator.hasNext()) {
                    String line = lineIterator.nextLine();
                    System.out.println(line);
                }
                this.exportDataInFile();
            } catch (Exception e) {
                log.error("Erro ao ler arquivo", e);
            }
        } else {
            log.info("Arquivo não existe ou não pode ser lido");
        }
    }

    private void exportDataInFile() {
        if (AppStartup.OUTPUT == null || AppStartup.OUTPUT.isEmpty()) {
            log.error("Path de output do relatorio não pode ser nulo ou vazio");
            return;
        }

        String filePath = AppStartup.OUTPUT + "relatorio.txt";
        File file = new File(filePath);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write("Teste".getBytes());
        } catch (IOException e) {
            log.error("Erro ao gerar relatório", e);
        }

    }
}
