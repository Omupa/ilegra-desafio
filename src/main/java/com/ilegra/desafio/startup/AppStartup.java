package com.ilegra.desafio.startup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import com.ilegra.desafio.repositories.FilesRepository;
import com.ilegra.desafio.services.FileProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AppStartup {

    private static final Logger log = LoggerFactory.getLogger(AppStartup.class);
    public static String INPUT = System.getProperty("desafio.inputdir");

    private final FileProcessService fileProcessService;

    public AppStartup(FileProcessService fileProcessService) {
        this.fileProcessService = fileProcessService;
    }

    @PostConstruct
    private void init() {
        Objects.requireNonNull(INPUT, "Input path can not be null");

        this.processExistingFiles();
        this.filesListener();
    }

    private void processExistingFiles() {
        Set<String> existingFiles = Stream.of(Objects.requireNonNull(new File(INPUT).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());

        FilesRepository.filesToProcess.addAll(existingFiles);
        fileProcessService.processFileList();
    }

    private void filesListener() {
        Path path = Paths.get(INPUT);

        try (WatchService watchService = path.getFileSystem().newWatchService()) {
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            boolean isValid = true;
            WatchKey watchKey = null;
            while (isValid) {
                watchKey = watchService.poll(10, TimeUnit.MINUTES);
                if (watchKey != null) {
                    watchKey.pollEvents().forEach(event -> FilesRepository.filesToProcess.add(event.context().toString()));
                    isValid = watchKey.reset();

                    fileProcessService.processTryAgainFiles();
                    fileProcessService.processFileList();
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error on listener folder", e);
        }
    }
}
