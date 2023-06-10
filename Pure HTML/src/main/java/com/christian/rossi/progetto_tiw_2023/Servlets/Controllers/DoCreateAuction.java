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
        final Set<Long> products;
        final int rise;
        final Long userID;
        final Timestamp expiry;

        //checking problem with variables product
        if (request.getParameterValues("product") == null) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.MISSING_PRODUCT).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        else {
            try {
                products = Arrays.stream(request.getParameterValues("product")).map(Long::parseLong).collect(Collectors.toUnmodifiableSet());
                //TODO: inputchecker
            }
            catch (NumberFormatException e) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NUMBER_FORMAT_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
                return;
            }
        }

        //checking problem with variable rise
        try {
            rise = Integer.parseInt(request.getParameter("rise"));
            if (!InputChecker.checkRise(rise)) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.RISE_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
                return;
            }
        }
        catch (NumberFormatException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NUMBER_FORMAT_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }

        //getter for the current userID
        userID = (Long) session.getAttribute("userID");

        //start of time parsing
        //TODO:check della data
        String expiryHtml = request.getParameter("expiry");
        String dateTimeString = expiryHtml.replace("T", " ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        expiry = Timestamp.valueOf(dateTime);
        if (!InputChecker.checkExpiry(expiry)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.EXPIRY_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        //end of time parsing

        //check if every product selected belongs to the user that made the request
        Iterator<Long> productsIterator = products.iterator();
        ProductDAO productDAO = new ProductDAO();
        while (productsIterator.hasNext()) {
            try {
                Long productID = productsIterator.next();
                if (productDAO.CheckProduct(productID, userID)) {
                    response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.PRODUCT_ID_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
                    return;
                }
            } catch (SQLException e) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
                return;
            }
        }

        //setting the price based on the price of the various articles
        productsIterator = products.iterator();
        int price = 0;
        while (productsIterator.hasNext()) {
            try {
                Long productID = productsIterator.next();
                price += productDAO.GetPrice(productID);
            } catch (SQLException e) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
                return;
            }
        }

        //auction creation and setting value for the various objects
        //TODO: fix per le query concatenate
        try {
            productsIterator = products.iterator();
            AuctionDAO auctionDAO = new AuctionDAO();
            Long auctionID = auctionDAO.createAuction(price, rise, expiry, userID);
            while (productsIterator.hasNext()) {
                Long productID = productsIterator.next();
                productDAO.update(productID, auctionID);
            }
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        response.sendRedirect(URLs.GET_SELL_PAGE);
    }
}