package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet(name = "DoClose", urlPatterns = {URLs.DO_CLOSE})
public class DoClose extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        final Long userID;
        final Long auctionID;

        //getter for the current userID
        userID = (Long) session.getAttribute("userID");

        //checking parse problems with variable auctionID
        try {
            auctionID = Long.valueOf((request.getParameter("close")));
        } catch (NumberFormatException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NUMBER_FORMAT_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
            return;
        }
        try {
            //update query only if the owner of the action is who is closing it
            AuctionDAO auctionDAO = new AuctionDAO();
            auctionDAO.close(auctionID, userID);
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
            return;
        }
        response.sendRedirect(URLs.GET_DETAILS_PAGE);
    }
}
