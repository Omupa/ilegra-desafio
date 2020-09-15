package com.ilegra.desafio.schedules;

import javax.annotation.PostConstruct;

import com.ilegra.desafio.services.FileProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FilesProcessSchedule {

    private static final Logger log = LoggerFactory.getLogger(FilesProcessSchedule.class);

    private final FileProcessService fileProcessService;

    public FilesProcessSchedule(FileProcessService fileProcessService) {
        this.fileProcessService = fileProcessService;
    }

    @PostConstruct
    private void init() {
        this.reportCurrentTime();
    }

    @Scheduled(fixedDelay = 1)
    public void reportCurrentTime() {
        if (!FileProcessService.processando) {
            log.info("Job iniciado");
            fileProcessService.processFileList();
        }
    }
}
