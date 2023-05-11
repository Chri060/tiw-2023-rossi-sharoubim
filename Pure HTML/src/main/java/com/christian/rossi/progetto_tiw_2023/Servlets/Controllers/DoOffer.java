package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

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
        final String productID = request.getParameter("auctionID");
        final Timestamp date = new Timestamp(System.currentTimeMillis());







        //TODO: non è controllato
        if (!InputChecker.checkOffer(Integer.parseInt(offer), 1, 1, 1)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DESCRIPTION_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }





        try {
            OfferDAO offerDAO = new OfferDAO();
            offerDAO.addOffer(offer, String.valueOf(userID), productID, date);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while trying to retrieve data from the database");
        }
        response.sendRedirect(URLs.GET_OFFERS_PAGE);
    }
}