package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DoClose", urlPatterns = {Constants.DO_CLOSE})
@MultipartConfig
public class DoClose extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        final Long userID;
        final Long auctionID;
        userID = (Long) session.getAttribute("userID");
        try {
            auctionID = Long.valueOf((request.getParameter("auctionID")));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            if (auctionDAO.isAuctionOwner(userID, auctionID)) {
                auctionDAO.close(auctionID, userID);
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(Constants.GET_AUCTION_DETAILS);
                requestDispatcher.forward(request, response);
            }
            else response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } catch (SQLException e) { response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); }
    }
}