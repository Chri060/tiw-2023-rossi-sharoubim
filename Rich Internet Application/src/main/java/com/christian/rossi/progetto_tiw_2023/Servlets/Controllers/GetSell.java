package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.JSONPrototypes.SellPageData;
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

@WebServlet(name = "GetSellPage", urlPatterns = {Constants.GET_SELL})
@MultipartConfig
public class GetSell extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            ProductDAO productDAO = new ProductDAO();
            AuctionDAO auctionDAO = new AuctionDAO();
            SellPageData sellPageData = new SellPageData();
            Long userID = (Long) session.getAttribute("userID");
            List<ProductBean> productBeanList = productDAO.getUserProducts(userID);
            sellPageData.setMyProducts(productBeanList);
            List<AuctionBean> openAuctions = auctionDAO.getUserAuctions(userID, 1, session.getCreationTime());
            for (AuctionBean auctionBean : openAuctions) auctionBean.setProductList(productDAO.getProductFromAuction(auctionBean.getAuctionID()));
            sellPageData.setMyOpenAuctions(openAuctions);
            List<AuctionBean> closedAuctions = auctionDAO.getUserAuctions(userID, 0, session.getCreationTime());
            for (AuctionBean auctionBean : closedAuctions) auctionBean.setProductList(productDAO.getProductFromAuction(auctionBean.getAuctionID()));
            sellPageData.setMyClosedAuctions(closedAuctions);
            Gson gson = new Gson();
            String json = gson.toJson(sellPageData);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
        catch (NullPointerException e) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST); }
        catch (SQLException e) { response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); }
    }
}