package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.DAOs.*;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet("/docreateauction")
public class DoCreateAuction extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        final Set<Long> products = Arrays.stream(request.getParameterValues("product")).map(Long::parseLong).collect(Collectors.toUnmodifiableSet());
        final int rise = Integer.parseInt(request.getParameter("rise"));
        final Long userID = (Long) session.getAttribute("userID");
        //start of time parsing
        String expiryHtml = request.getParameter("expiry");
        String dateTimeString = expiryHtml.replace("T", " ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        final Timestamp expiry = Timestamp.valueOf(dateTime);
        //end of time parsing
        Iterator<Long> productsIterator = products.iterator();
        int price = 0;
        while (productsIterator.hasNext()) {
            try {
                ProductDAO productDAO = new ProductDAO();
                Long articleID = productsIterator.next();
                price += productDAO.GetPrice(articleID);
            } catch (SQLException e) {  throw new RuntimeException(e); }
        }
        productsIterator = products.iterator();
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            Long auctionID = auctionDAO.createAuction(price, rise, expiry, userID);
            ProductDAO productDAO = new ProductDAO();
            while (productsIterator.hasNext()) {
                Long articleID = productsIterator.next();
                productDAO.update(articleID, auctionID);
            }
        } catch (SQLException e) {
            //TODO: pagina di errore per problemi con la query
        }
        response.sendRedirect("/sell");
    }
}