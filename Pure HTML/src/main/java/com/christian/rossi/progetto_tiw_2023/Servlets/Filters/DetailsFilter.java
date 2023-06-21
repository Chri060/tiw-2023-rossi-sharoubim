package com.christian.rossi.progetto_tiw_2023.Servlets.Filters;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebFilter(filterName = "OfferFilter", urlPatterns = {URLs.GET_DETAILS_PAGE, URLs.DO_CLOSE})
public class DetailsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;
        HttpSession session = hrequest.getSession();
        Long userID = (Long) session.getAttribute("userID");
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            final Long auctionID;
            try {
                auctionID = Long.valueOf(request.getParameter("details"));
                List<AuctionBean> list = auctionDAO.getAuctionsByID(auctionID, 1);
                if (list == null || list.get(0).getUserID().equals(userID)) {
                    hresponse.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.AUCTION_ERROR).addParam("redirect", URLs.GET_HOME_PAGE).toString());
                    return;
                }
            }
            catch (NumberFormatException e) {
                hresponse.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.AUCTION_ERROR).addParam("redirect", URLs.GET_HOME_PAGE).toString());
            }
        } catch (SQLException e) {
            hresponse.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_HOME_PAGE).toString());
            return;
        }
        //filter chain pattern
        chain.doFilter(request, response);
    }
}