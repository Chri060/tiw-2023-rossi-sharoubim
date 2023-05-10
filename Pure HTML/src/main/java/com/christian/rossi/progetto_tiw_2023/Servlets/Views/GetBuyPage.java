package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "GetBuyPage", urlPatterns = {URLs.GET_BUY_PAGE})
public class GetBuyPage extends ThymeleafHTTPServlet {

    private String article;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        final String template = "buy";
        final Long userID = (Long) session.getAttribute("userID");
        try {
             AuctionDAO auctionDAO = new AuctionDAO();
             ctx.setVariable("auctions", auctionDAO.getAuctionByKeyword(article, userID, session.getCreationTime()));
             ctx.setVariable("closedauctions", auctionDAO.getWonAuctions((Long) session.getAttribute("userID")));
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while trying to set values for the page");
            throw new RuntimeException(e);
        }
        getTemplateEngine().process(template, ctx, response.getWriter());
        article = null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        article = request.getParameter("search");
        doGet(request, response);
    }
}