package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;
import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "GetDetailsPage", urlPatterns = {URLs.GET_DETAILS_PAGE})
public class GetDetailsPage extends ThymeleafHTTPServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        final Long userID = (Long) session.getAttribute("userID");
        final String template = "details";
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        try {
            Long auctionID = Long.valueOf(request.getParameter("details"));
            OfferDAO offerDAO = new OfferDAO();
            UserDAO userDAO = new UserDAO();
            AuctionDAO auctionDAO = new AuctionDAO();
            if (!auctionDAO.isAuctionOwner(userID, auctionID)) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.GENERIC_ERROR).addParam("redirect", URLs.GET_BUY_PAGE).toString());
                return;
            }
            List<AuctionBean> auctionBeanList = auctionDAO.getAuctionbyID(auctionID, session.getCreationTime());
            ProductDAO productDAO = new ProductDAO();
            List<ProductBean> productBeanList = productDAO.getProductFromAuction(auctionBeanList.get(0).getAuctionID());
            UserBean winner = null;
            if (auctionDAO.getWinner(auctionID) != null) {
                Long winnerID = auctionDAO.getWinner(auctionID);
                winner = userDAO.getUser(winnerID);
            }
            ctx.setVariable("selectedauction", productBeanList);
            ctx.setVariable("offer",  offerDAO.getOffers(auctionID));
            ctx.setVariable("winner", winner);
            ctx.setVariable("close", auctionID);
            ctx.setVariable("active", auctionBeanList.get(0).isActive());
            if (winner != null) ctx.setVariable("user", userDAO.getUser(winner.getUserID()));
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
        } catch (NumberFormatException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NUMBER_FORMAT_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
        }
        getTemplateEngine().process(template, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doGet(request, response);
    }
}