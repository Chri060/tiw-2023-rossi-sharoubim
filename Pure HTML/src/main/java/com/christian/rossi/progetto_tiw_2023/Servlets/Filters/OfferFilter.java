package com.christian.rossi.progetto_tiw_2023.Servlets.Filters;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebFilter(filterName = "OfferFilter", urlPatterns = {URLs.GET_OFFERS_PAGE, URLs.DO_OFFER})
public class OfferFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse hresponse = (HttpServletResponse) response;
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            final Long auctionID;
            try {
                auctionID = Long.valueOf(request.getParameter("details"));
                List<AuctionBean> list = auctionDAO.getAuctionsByID(auctionID, 1);
                if (list == null || !list.get(0).isActive()) {
                    hresponse.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.OFFER_AUTH).addParam("redirect", URLs.GET_BUY_PAGE).toString());
                    return;
                }
            }
            catch (NumberFormatException e) {
                hresponse.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.OFFER_AUTH).addParam("redirect", URLs.GET_BUY_PAGE).toString());
            }
        } catch (SQLException e) {
            hresponse.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_BUY_PAGE).toString());
            return;
        }
        //filter chain pattern
        chain.doFilter(request, response);
    }
}