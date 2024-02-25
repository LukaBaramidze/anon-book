package com.mziuri;

public class PurchaseRequest {
    private String name;
    private Integer amount;

    public String getName() {
        return name;
    }

    public PurchaseRequest() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public PurchaseRequest(String name, Integer amount) {
        this.name = name;
        this.amount = amount;
    }
}
