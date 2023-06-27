package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

@WebServlet(name = "DoOffer", urlPatterns = {URLs.DO_OFFER})
public class DoOffer extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        final String offer;
        final Long userID;
        final Long auctionID;
        final Timestamp date;
        userID = (Long) session.getAttribute("userID");
        try {
            offer = request.getParameter("offer");
            auctionID = Long.valueOf(request.getParameter("details"));
            if (offer == null) throw new NullPointerException();
        }
        catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NUMBER_FORMAT_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
            return;
        }
        date = new Timestamp(System.currentTimeMillis());
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            OfferDAO offerDAO = new OfferDAO();
            AuctionBean auctionBean = auctionDAO.getAuctionByID(auctionID, session.getCreationTime());
            int start = auctionBean.getPrice();
            int rise = auctionBean.getRise();
            int actualOffer = offerDAO.getMaxOffer(auctionID);
            if (!InputChecker.checkOffer(Integer.parseInt(offer), start, rise, actualOffer)) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.OFFER_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE + "?details=" + auctionID).toString());
                return;
            }
            if (Objects.equals(auctionBean.getUserID(), userID)) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.OFFER_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
                return;
            }
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
            return;
        }
        catch (NullPointerException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.GENERIC_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
            return;
        }
        try {
            OfferDAO offerDAO = new OfferDAO();
            offerDAO.addOffer(offer, userID, auctionID, date);
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
            return;
        }
        request.setAttribute("details", auctionID);
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(URLs.GET_OFFERS_PAGE);
        requestDispatcher.forward(request, response);
    }
}