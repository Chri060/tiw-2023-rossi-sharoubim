package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;


@WebServlet(name = "DoOffer", urlPatterns = {URLs.DO_OFFER})
public class DoOffer extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //TODO: controllo input
        HttpSession session = request.getSession();
        final String offer = request.getParameter("offer");
        final Long userID = (Long) session.getAttribute("userID");
        final Long auctionID = Long.valueOf(request.getParameter("auctionID"));
        final Timestamp date = new Timestamp(System.currentTimeMillis());
        try {
            OfferDAO offerDAO = new OfferDAO();
            offerDAO.addOffer(offer, String.valueOf(userID), String.valueOf(auctionID), date);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while trying to retrieve data from the database");
        }
        response.sendRedirect("/offers");
    }
}