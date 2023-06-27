package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.google.gson.Gson;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "GetMatchingAuctions", urlPatterns = {Constants.GET_MATCHING_AUCTIONS})
@MultipartConfig
public class GetMatchingAuctions extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Long userID = (Long) session.getAttribute("userID");

        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            ProductDAO productDAO = new ProductDAO();
            String article = request.getParameter("article");
            if (article.equals(""))  throw  new NullPointerException();
            List<AuctionBean> auctionBeanList = auctionDAO.getAuctionByKeyword(article.toLowerCase(), userID, session.getCreationTime());
            for (AuctionBean auction : auctionBeanList) auction.setProductList(productDAO.getProductFromAuction(auction.getAuctionID()));
            Gson gson = new Gson();
            String json = gson.toJson(auctionBeanList);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
        catch (NullPointerException e) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST); }
        catch (SQLException e) { response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); }
    }
}
