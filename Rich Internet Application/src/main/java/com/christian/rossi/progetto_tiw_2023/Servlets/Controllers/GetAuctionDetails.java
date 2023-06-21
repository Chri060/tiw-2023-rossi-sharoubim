package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.Constants.*;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.JSONPrototypes.AuctionData;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "GetAuctionDetails", urlPatterns = {Constants.GET_AUCTION_DETAILS})
@MultipartConfig
public class GetAuctionDetails extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            Long auctionID = Long.parseLong(request.getParameter("auctionID"));
            AuctionDAO auctionDAO = new AuctionDAO();
            OfferDAO offerDAO = new OfferDAO();
            UserDAO userDAO = new UserDAO();
            AuctionData auctionData = new AuctionData();

            auctionData.setProductList(auctionDAO.getAuctionsByID(auctionID, session.getCreationTime()));
            auctionData.setOffersList(offerDAO.getOffers(auctionID));
            if (auctionDAO.getWinner(auctionID) != null) {
                Long winnerID = auctionDAO.getWinner(auctionID).getUserID();
                UserBean winner = userDAO.getUser(winnerID.toString());
                auctionData.setWinner(winner);
            }

            Gson gson = new Gson();
            String json = gson.toJson(auctionData);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);


        } catch (NumberFormatException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
