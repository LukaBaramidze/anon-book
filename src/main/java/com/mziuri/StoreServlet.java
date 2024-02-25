package com.mziuri;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class StoreServlet extends HttpServlet {

    public static String[] getNames(List<Product> productList) {
        String[] nameArr = new String[productList.size()];
        for (int i = 0; i < productList.size(); i++) {
            nameArr[i] = productList.get(i).getProd_name();
        }
        return nameArr;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseManager instance = DatabaseManager.getInstance();
        List<Product> prod_list = instance.select();
        GetProductsResponse productNameList = new GetProductsResponse(getNames(prod_list));
        String jsonProdList = new ObjectMapper().writeValueAsString(productNameList);
        resp.setContentType("application/json");
        resp.getWriter().write(jsonProdList);
    }
}
