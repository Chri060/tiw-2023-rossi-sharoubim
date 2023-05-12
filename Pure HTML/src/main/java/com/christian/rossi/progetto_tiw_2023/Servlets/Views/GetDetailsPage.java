package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
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
import java.util.List;

@WebServlet(name = "GetDetailsPage", urlPatterns = {URLs.GET_DETAILS_PAGE})
public class GetDetailsPage extends ThymeleafHTTPServlet {

    private String details;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        final String template = "details";
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            OfferDAO offerDAO = new OfferDAO();
            UserDAO userDAO = new UserDAO();
            List<AuctionBean> auctionBeanList = auctionDAO.getAuctionsByID(details, session.getCreationTime());
            AuctionBean winner = auctionDAO.getWinner(details);
            ctx.setVariable("selectedauction", auctionBeanList);
            ctx.setVariable("offer", offerDAO.getOffers(details));
            ctx.setVariable("winner", winner);
            ctx.setVariable("close", details);
            ctx.setVariable("active", auctionBeanList.get(0).isActive());
            if (winner != null) {
                ctx.setVariable("user", userDAO.getUser(String.valueOf(winner.getUserID())));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            throw new RuntimeException(e);
        }
        getTemplateEngine().process(template, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        details = request.getParameter("details");
        this.doGet(request, response);
    }
}