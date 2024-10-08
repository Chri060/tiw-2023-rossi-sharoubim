package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Beans.OfferBean;
import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.Constants.*;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.JSONPrototypes.AuctionData;
import com.google.gson.Gson;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "GetSearchedAuctionDetails", urlPatterns = {Constants.GET_SEARCHED_AUCTION_DETAILS})
@MultipartConfig
public class GetSearchedAuctionDetails extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Long userID = (Long) session.getAttribute("userID");
        try {
            Long auctionID = Long.parseLong(request.getParameter("auctionID"));
            AuctionDAO auctionDAO = new AuctionDAO();
            OfferDAO offerDAO = new OfferDAO();
            UserDAO userDAO = new UserDAO();
            ProductDAO productDAO = new ProductDAO();
            AuctionData auctionData = new AuctionData();
            if (!auctionDAO.isAuctionActive(auctionID) && !userID.equals(auctionDAO.getWinner(auctionID))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            auctionData.setActive(auctionDAO.isAuctionActive(auctionID));
            auctionData.setProductList(productDAO.getProductFromAuction(auctionID));
            List<OfferBean> offerList= offerDAO.getOffers(auctionID);
            for (OfferBean offer : offerList) offer.setUserName(userDAO.getUser(offer.getUserID().toString()).getUsername());
            auctionData.setOffersList(offerList);
            if (auctionDAO.getWinner(auctionID) != null) {
                Long winnerID = auctionDAO.getWinner(auctionID);
                UserBean winner = userDAO.getUser(winnerID.toString());
                auctionData.setWinner(winner);
            }
            AuctionBean auctionBean = auctionDAO.getAuctionByID(auctionID, session.getCreationTime());
            auctionData.setRise(auctionBean.getRise());
            auctionData.setPrice(auctionBean.getPrice());
            Gson gson = new Gson();
            String json = gson.toJson(auctionData);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
        catch (NumberFormatException | NullPointerException e) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST); }
        catch (SQLException e) { response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
