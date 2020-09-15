package com.ilegra.desafio.startup;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.ilegra.desafio.services.FileProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AppStartup {

    private static final Logger log = LoggerFactory.getLogger(AppStartup.class);

    public static String INPUT = System.getProperty("desafio.inputdir");
    public static String OUTPUT = System.getProperty("desafio.outputdir");

    private final FileProcessService fileProcessService;

    public AppStartup(FileProcessService fileProcessService) {
        this.fileProcessService = fileProcessService;
    }

    @PostConstruct
    private void init() {
        this.processExistingFiles();
        this.filesListener();
    }

    private void filesListener() {
        Objects.requireNonNull(INPUT, "O path de input não pode ser nulo.");

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(INPUT);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    FileProcessService.filesToProcess.add(event.context().toString());
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            log.error("Erro no listener do path de input", e);
        }
    }

    private void processExistingFiles() {
        Objects.requireNonNull(INPUT, "O path de input não pode ser nulo.");

        Set<String> existingFiles = Stream.of(Objects.requireNonNull(new File(INPUT).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());

        FileProcessService.filesToProcess.addAll(existingFiles);
        fileProcessService.processFileList();
    }
}
