package com.mziuri;

public class GetProductsResponse {
    private String[] productNames;

    public GetProductsResponse(String[] productNames) {
        this.productNames = productNames;
    }

    public String[] getProductNames() {
        return productNames;
    }

    public void setProductNames(String[] productNames) {
        this.productNames = productNames;
    }
}
