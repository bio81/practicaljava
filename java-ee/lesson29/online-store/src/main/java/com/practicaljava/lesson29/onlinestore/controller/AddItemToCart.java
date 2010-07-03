package com.practicaljava.lesson29.onlinestore.controller;

import com.practicaljava.lesson29.onlinestore.Message;
import com.practicaljava.lesson29.onlinestore.RequestParameters;
import com.practicaljava.lesson29.onlinestore.model.Cart;
import com.practicaljava.lesson29.onlinestore.model.Item;
import com.practicaljava.lesson29.onlinestore.service.ItemService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addItem")
public class AddItemToCart extends HttpServlet {

    @EJB
    private ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
                                                                                          ServletException {

        Long productCode = RequestParameters.getLong(request, "productCode");

        if (productCode == null) {
            Message.setErrorMessage(request, "Item code is not correct");
            response.sendRedirect("items");
            return;
        }

        Item item = itemService.itemByCode(productCode);

        if (item == null) {
            Message.setErrorMessage(request, "Item with code '" + productCode + "' was not found");
            response.sendRedirect("items");
            return;
        }

        Cart cart = (Cart) request.getSession().getAttribute("cart");
        cart.addItem(item);

        Message.setInfoMessage(request, "Item '" + item.getName() + "' was added to cart");
        response.sendRedirect("items");
    }
}