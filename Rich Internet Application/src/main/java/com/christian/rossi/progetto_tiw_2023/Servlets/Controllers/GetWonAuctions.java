package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;


import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "GetWonAuctions", urlPatterns = {Constants.GET_WON_AUCTIONS})
public class GetWonAuctions  extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            ProductDAO productDAO = new ProductDAO();
            Long userID = (Long) session.getAttribute("userID");
            List<AuctionBean> auctionBeanList = auctionDAO.getWonAuctions(userID);
            for (AuctionBean auction : auctionBeanList) {
                auction.setProductList(productDAO.getProductFromAuction(auction.getAuctionID()));
            }

            Gson gson = new Gson();
            String json = gson.toJson(auctionBeanList);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

}