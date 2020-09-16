package com.ilegra.desafio.repositories;

import java.util.LinkedList;
import java.util.Queue;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class FilesRepository {
    public static final Queue<String> filesToProcess = new LinkedList<>();
    public static final Queue<String> failedFiles = new LinkedList<>();
    public static final Queue<String> tryAgain = new LinkedList<>();

    public static void clear() {
        filesToProcess.clear();
        failedFiles.clear();
    }
}
