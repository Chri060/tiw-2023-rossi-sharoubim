package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
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
import java.util.List;

import static java.lang.Long.parseLong;

@WebServlet(name = "GetOffersPage", urlPatterns = {URLs.GET_OFFERS_PAGE})
public class GetOffersPage extends ThymeleafHTTPServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        final Long userID = (Long) session.getAttribute("userID");
        Long auctionID = null;
        try {
            auctionID = Long.valueOf(request.getParameter("details"));
        }
        catch (NumberFormatException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NUMBER_FORMAT_ERROR).addParam("redirect", URLs.GET_BUY_PAGE).toString());
        }
        final String template = "offers";
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        try {
            OfferDAO offerDAO = new OfferDAO();
            ProductDAO productDAO = new ProductDAO();
            AuctionDAO auctionDAO = new AuctionDAO();
            if (auctionDAO.isAuctionOwner(userID, auctionID)) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.GENERIC_ERROR).addParam("redirect", URLs.GET_BUY_PAGE).toString());
                return;
            }
            List<ProductBean> productBeanlist = productDAO.getProductFromAuction(auctionID);
            List<AuctionBean> auctionBean = auctionDAO.getAuctionbyID(auctionID, session.getCreationTime());
            boolean active = auctionDAO.isAuctionActive(auctionID);
            ctx.setVariable("price", auctionBean.get(0));
            ctx.setVariable("auction", productBeanlist);
            ctx.setVariable("offer", offerDAO.getOffers(auctionID));
            ctx.setVariable("actualID", auctionID);
            ctx.setVariable("active", active);
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_BUY_PAGE).toString());
            throw new RuntimeException(e);
        }
        getTemplateEngine().process(template, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doGet(request, response);
    }
}