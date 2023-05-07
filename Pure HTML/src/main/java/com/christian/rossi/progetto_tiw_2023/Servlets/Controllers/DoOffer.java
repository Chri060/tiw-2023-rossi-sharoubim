package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/dooffer")
public class DoOffer extends ThymeleafHTTPServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        String offer = request.getParameter("offer");
        Long userID = (Long) session.getAttribute("userID");
        String auctionID = request.getParameter("auctionID");

        OfferDAO offerDAO = null;
        try {
            offerDAO = new OfferDAO();
            offerDAO.addOffer(offer, String.valueOf(userID), auctionID);
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO: pagina di errore (connessione al DB o query)
        }
        response.sendRedirect("/offers");
    }
}