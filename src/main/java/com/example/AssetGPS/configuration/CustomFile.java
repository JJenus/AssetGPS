package com.example.AssetGPS.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class CustomFile {
    public String getTemplate(String file) {
        File resource = null;
        String template = "";
        try {
            resource = new File("templates/"+file);
            template = new String(Files.readAllBytes(resource.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return template;
    }

    public void write(String file, String data){
        File resource = new File("templates/" + file);
        byte[] strToBytes = data.getBytes();
        try {
            resource.getParentFile().mkdirs(); // Will create parent directories if not exists
            resource.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(resource,false);

           outputStream.write(strToBytes);
//           System.out.println(resource.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
