package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/doClose")
public class DoClose extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String auctionID = request.getParameter("close");
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            auctionDAO.close(auctionID);
        } catch (SQLException e) {
            //TODO: pagina di errore per problemi con la query
        }
        response.sendRedirect("/details");
    }
}
