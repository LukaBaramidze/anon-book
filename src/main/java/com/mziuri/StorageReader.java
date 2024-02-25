package com.mziuri;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class StorageReader {
    private static StorageReader instance;

    private StorageReader() {
    }

    public static StorageReader getInstance() {
        if (instance == null) {
            synchronized (StorageReader.class) {
                if (instance == null) {
                    instance = new StorageReader();
                }
            }
        }
        return instance;
    }

    public void readJSON() throws IOException {
        InputStream inputStream = StorageConfig.class.getClassLoader().getResourceAsStream("storage.json");
        ObjectMapper mapper = new ObjectMapper();
        StorageConfig storageConfig = mapper.readValue(inputStream, StorageConfig.class);
        Product[] products = storageConfig.getProducts();
        DatabaseManager temp = DatabaseManager.getInstance();
        for (Product product : products) {
            temp.save(product);
        }
    }
}