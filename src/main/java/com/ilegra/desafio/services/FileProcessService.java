package com.ilegra.desafio.services;

import com.ilegra.desafio.startup.AppStartup;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FileProcessService {

    public void readFile(String filePath) throws FileNotFoundException {
        String path = AppStartup.INPUT + filePath;
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        try (BufferedReader br = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
