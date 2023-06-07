package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;


@WebServlet(name = "DoOffer", urlPatterns = {URLs.DO_OFFER})
public class DoOffer extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        final String offer = request.getParameter("offer");
        final Long userID = (Long) session.getAttribute("userID");
        final String auctionID = request.getParameter("details");
        final Timestamp date = new Timestamp(System.currentTimeMillis());
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            OfferDAO offerDAO = new OfferDAO();
            List<AuctionBean> auctionBeanList = auctionDAO.getAuctionsByID(auctionID, session.getCreationTime());
            int start = auctionBeanList.get(0).getPrice();
            int rise = auctionBeanList.get(0).getRise();
            int actualOffer = offerDAO.getMaxOffer(auctionID);
            if (!InputChecker.checkOffer(Integer.parseInt(offer), start, rise, actualOffer)) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.OFFER_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
                return;
            }
            if (Objects.equals(auctionBeanList.get(0).getUserID(), userID)) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.OFFER_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            OfferDAO offerDAO = new OfferDAO();
            offerDAO.addOffer(offer, String.valueOf(userID), auctionID, date);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
        }
        response.sendRedirect(URLs.GET_OFFERS_PAGE);
    }
}