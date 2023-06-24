package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;


@WebServlet(name = "DoOffer", urlPatterns = {Constants.DO_OFFER})
@MultipartConfig
public class DoOffer extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Integer offer;
        final Long userID;
        final Long auctionID;
        final Timestamp date;

        AuctionDAO auctionDAO = new AuctionDAO();
        OfferDAO offerDAO = new OfferDAO();

        try {
            //Getting variable auctionID
            auctionID = Long.valueOf(request.getParameter("auctionID"));
            //getting the value of offer
            offer = Integer.parseInt(request.getParameter("offer"));
            //getting the value of userID
            userID = (Long) session.getAttribute("userID");
            //getting the current date
            date = new Timestamp(System.currentTimeMillis());
            AuctionBean auctionBean = auctionDAO.getAuctionByID(auctionID, session.getCreationTime());
            int start = auctionBean.getPrice();
            int rise = auctionBean.getRise();
            int actualOffer = offerDAO.getMaxOffer(auctionID);
            //Caso offerta troppo bassa
            if (!InputChecker.checkOffer(offer, start, rise, actualOffer)) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                return;
            }
            //Caso offerta alla mia stessa asta
            if (Objects.equals(auctionBean.getUserID(), userID)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            offerDAO.addOffer(offer.toString(), userID, auctionID, date);
            request.setAttribute("details", auctionID);
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(Constants.GET_SEARCHED_AUCTION_DETAILS);
            requestDispatcher.forward(request, response);
        }
        catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        catch (NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }
}