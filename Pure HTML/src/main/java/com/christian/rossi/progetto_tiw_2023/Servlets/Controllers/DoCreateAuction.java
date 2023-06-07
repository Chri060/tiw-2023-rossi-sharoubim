package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.*;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

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

@WebServlet(name = "DoCreateAuction", urlPatterns = {URLs.DO_CREATE_AUCTION})
public class DoCreateAuction extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (request.getParameterValues("product") == null) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.MISSING_PRODUCT).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
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
        if (!InputChecker.checkRise(rise)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.RISE_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        if (!InputChecker.checkExpiry(expiry)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.EXPIRY_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        Iterator<Long> productsIterator = products.iterator();
        while (productsIterator.hasNext()) {
            try {
                ProductDAO productDAO = new ProductDAO();
                Long productID = productsIterator.next();
                if (productDAO.CheckProduct(productID, userID)) {
                    response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.PRODUCT_ID_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
                return;
            }
        }
        productsIterator = products.iterator();
        int price = 0;
        while (productsIterator.hasNext()) {
            try {
                ProductDAO productDAO = new ProductDAO();
                Long productID = productsIterator.next();
                price += productDAO.GetPrice(productID);
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
                return;
            }
        }
        productsIterator = products.iterator();
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            Long auctionID = auctionDAO.createAuction(price, rise, expiry, userID);
            ProductDAO productDAO = new ProductDAO();
            while (productsIterator.hasNext()) {
                Long productID = productsIterator.next();
                productDAO.update(productID, auctionID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        response.sendRedirect(URLs.GET_SELL_PAGE);
    }
}