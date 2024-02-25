package com.mziuri;

public class AddProductResponse {
    private String name;
    private Integer remainingAmount;

    public String getName() {
        return name;
    }

    public AddProductResponse() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public AddProductResponse(String name, Integer remainingAmount) {
        this.name = name;
        this.remainingAmount = remainingAmount;
    }
}
