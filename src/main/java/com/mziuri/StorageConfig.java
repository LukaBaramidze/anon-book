package com.mziuri;

public class StorageConfig {
    private Product[] products;
    private String password;

    public StorageConfig() {
    }

    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StorageConfig(Product[] products, String password) {
        this.products = products;
        this.password = password;
    }
}
