package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DoClose", urlPatterns = {Constants.DO_CLOSE})
public class DoClose extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        final Long userID;
        final Long auctionID;

        //getter for the current userID
        userID = (Long) session.getAttribute("userID");

        //checking parse problems with variable auctionID
        try {
            auctionID = Long.valueOf((request.getParameter("auctionID")));
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            //update query only if the owner of the action is who is closing it
            AuctionDAO auctionDAO = new AuctionDAO();
            if (auctionDAO.isAuctionOwner(userID, auctionID)) {
                auctionDAO.close(auctionID, userID);
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }
}
