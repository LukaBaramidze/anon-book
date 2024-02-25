package com.mziuri;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ProductServlet extends HttpServlet {

    // Handles GET requests
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        DatabaseManager instance = DatabaseManager.getInstance();
        Product prod = instance.find(name);

        if (Objects.isNull(prod)) {
            resp.getWriter().write("Error 405: product doesn't exist.");
        } else {
            GetProductInfoResponse productInfoResponse = new GetProductInfoResponse(prod.getProd_name(), prod.getProd_price(), prod.getProd_amount());
            String jsonProdInfo = new ObjectMapper().writeValueAsString(productInfoResponse);
            resp.setContentType("application/json");
            resp.getWriter().write(jsonProdInfo);
        }
    }

    // Handles POST requests
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PurchaseRequest purchaseRequest = objectMapper.readValue(req.getReader(), PurchaseRequest.class);

        DatabaseManager instance = DatabaseManager.getInstance();

        if (Objects.isNull(instance.find(purchaseRequest.getName()))) {
            resp.getWriter().write("Error 405: product doesn't exist");
        } else if (instance.find(purchaseRequest.getName()).getProd_amount() == 0) {
            resp.getWriter().write("Error 405: product is out of stock");
        } else if (purchaseRequest.getAmount() > instance.find(purchaseRequest.getName()).getProd_amount()) {
            resp.getWriter().write("Error 402: not enough product in stock, amount remaining:" + instance.find(purchaseRequest.getName()).getProd_amount());
        } else {
            Integer remainAmount = instance.find(purchaseRequest.getName()).getProd_amount() - purchaseRequest.getAmount();
            instance.request(purchaseRequest.getName(), remainAmount);
            PurchaseResponse purchaseResponse = new PurchaseResponse(purchaseRequest.getName(), remainAmount);
            String jsonPurchaseResponse = objectMapper.writeValueAsString(purchaseResponse);
            resp.setContentType("application/json");
            resp.getWriter().write(jsonPurchaseResponse);
        }
    }

    // Handles PUT requests
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        AddProductRequest addProductRequest = objectMapper.readValue(req.getReader(), AddProductRequest.class);

        InputStream inputStream = Product.class.getClassLoader().getResourceAsStream("storage.json");
        JsonNode rootNode = objectMapper.readTree(inputStream);
        String password = rootNode.get("password").asText();

        if (password.equals(addProductRequest.getPassword())) {
            DatabaseManager instance = DatabaseManager.getInstance();

            if (Objects.isNull(instance.find(addProductRequest.getName()))) {
                resp.getWriter().write("Error 405: product doesn't exist");
            } else if (addProductRequest.getAmount() < 0){
                resp.getWriter().write("Error 407: incorrect quantity");
            } else {
                instance.request(addProductRequest.getName(), addProductRequest.getAmount());
                AddProductResponse productResponse = new AddProductResponse(addProductRequest.getName(), addProductRequest.getAmount());
                String jsonProductResponse = objectMapper.writeValueAsString(productResponse);
                resp.setContentType("application/json");
                resp.getWriter().write(jsonProductResponse);
            }
        } else {
            resp.getWriter().write("Error 403: password is incorrect, try again!");
        }
    }
}