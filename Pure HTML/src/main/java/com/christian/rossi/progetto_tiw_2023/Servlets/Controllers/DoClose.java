package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/doclose")
public class DoClose extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String auctionID = request.getParameter("close");
        AuctionDAO auctionDAO = null;
        try {
            auctionDAO = new AuctionDAO();
            auctionDAO.close(auctionID);
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO: pagina di errore (connessione al DB o query)
        }
        response.sendRedirect("/details");
    }
}
