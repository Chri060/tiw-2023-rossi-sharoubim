package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;


import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(name = "DoCreateAuction", urlPatterns = {Constants.DO_CREATE_AUCTION})
@MultipartConfig
public class DoCreateAuction extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        final Set<Long> products;
        final int rise;
        final Long userID;
        final Timestamp expiry;
        int statusCode = 0;

        //checking problem with variables product
        if (request.getParameterValues("product") == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        else {
            try {
                products = Arrays.stream(request.getParameterValues("product")).map(Long::parseLong).collect(Collectors.toUnmodifiableSet());
            }
            catch (NumberFormatException | NullPointerException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        //checking problem with variable rise
        try {
            rise = Integer.parseInt(request.getParameter("rise"));
            if (!InputChecker.checkRise(rise)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //getter for the current userID
        userID = (Long) session.getAttribute("userID");

        //start of time parsing
        try {
            String expiryHtml = request.getParameter("expiry");
            String dateTimeString = expiryHtml.replace("T", " ");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
            expiry = Timestamp.valueOf(dateTime);
            if (!InputChecker.checkExpiry(expiry)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } catch (DateTimeParseException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }

        //auction creation and setting value for the various objects
        AuctionDAO auctionDAO = new AuctionDAO();
        try {
            productsIterator = products.iterator();
            auctionDAO.setAutoCommit(false);
            productDAO.setAutoCommit(false);
            Long auctionID = auctionDAO.createAuction(price, rise, expiry, userID);
            while (productsIterator.hasNext()) {
                Long productID = productsIterator.next();
                productDAO.update(productID, auctionID);
            }
        } catch (SQLException e) {
            statusCode = -1;
            try {
                auctionDAO.rollback();
                productDAO.rollback();
            } catch (SQLException exception) {}
        }
        finally {
            try {
                auctionDAO.setAutoCommit(true);
                productDAO.setAutoCommit(true);
            } catch (SQLException e) {}
        }
        switch (statusCode) {
            case 0 -> response.setStatus(HttpServletResponse.SC_OK);
            case -1 -> response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
