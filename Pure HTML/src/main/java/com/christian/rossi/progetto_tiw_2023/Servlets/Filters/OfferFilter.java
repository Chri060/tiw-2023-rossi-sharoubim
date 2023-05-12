package com.christian.rossi.progetto_tiw_2023.Servlets.Filters;

import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebFilter(filterName = "OfferFilter", urlPatterns = {URLs.GET_OFFERS_PAGE, URLs.DO_OFFER})
public class OfferFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse hresponse = (HttpServletResponse) response;
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            final String auctionID = request.getParameter("details");
            if (auctionID == null || auctionDAO.getAuctionsByID(auctionID, 1) != null && !auctionDAO.getAuctionsByID(auctionID, 1).get(0).isActive()) {
                hresponse.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.AUTH_ERROR).addParam("redirect", URLs.GET_BUY_PAGE).toString());
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            hresponse.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_BUY_PAGE).toString());
            return;
        }
        //filter chain pattern
        chain.doFilter(request, response);
    }
}