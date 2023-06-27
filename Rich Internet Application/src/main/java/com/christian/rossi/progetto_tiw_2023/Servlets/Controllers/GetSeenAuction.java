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
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "GetSeenAuction", urlPatterns = {Constants.GET_SEEN_AUCTION})
@MultipartConfig
public class GetSeenAuction extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        try {
            Long userID = (Long) session.getAttribute("userID");
            final Set<Long> auctionIDArray;
            if (request.getParameterValues("auctionsIds") == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            else auctionIDArray = Arrays.stream(request.getParameterValues("auctionsIds")).map(Long::parseLong).collect(Collectors.toUnmodifiableSet());
            AuctionDAO auctionDAO = new AuctionDAO();
            ProductDAO productDAO = new ProductDAO();
            List<AuctionBean> auctionBeanList = new ArrayList<>();
            Iterator<Long> auctionsIterator = auctionIDArray.iterator();
            while (auctionsIterator.hasNext()) {
                Long auctionID = auctionsIterator.next();
                if (auctionDAO.isAuctionActive(auctionID)) {
                    AuctionBean auctionBean;
                    auctionBean = auctionDAO.getAuctionByID(auctionID, userID);
                    auctionBean.setProductList(productDAO.getProductFromAuction(auctionBean.getAuctionID()));
                    auctionBeanList.add(auctionBean);
                }
            }
            Gson gson = new Gson();
            String json = gson.toJson(auctionBeanList);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
        catch (NullPointerException | NumberFormatException e) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST); }
        catch (SQLException e) { response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); }
    }
}