package com.ilegra.desafio.startup;

import com.ilegra.desafio.services.FileProcessService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;

@Component
public class AppStartup {

    public static final String INPUT = "/home/omupa/Documentos/data/in/";
    private static final String OUTPUT = "/home/omupa/Documentos/data/out/";

    private final FileProcessService fileProcessService;

    public AppStartup(FileProcessService fileProcessService) {
        this.fileProcessService = fileProcessService;
    }

    @PostConstruct
    public void filesListener() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(INPUT);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
                    fileProcessService.readFile(event.context().toString());
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {

        }
    }
}
