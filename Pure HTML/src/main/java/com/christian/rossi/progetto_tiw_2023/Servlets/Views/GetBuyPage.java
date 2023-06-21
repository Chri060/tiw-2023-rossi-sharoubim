package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String article = request.getParameter("search");
        HttpSession session = request.getSession();
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        final String template = "buy";
        final Long userID;

        //getting the value of variable userID
        userID = (Long) session.getAttribute("userID");

        try {
             AuctionDAO auctionDAO = new AuctionDAO();
             ctx.setVariable("auctions", auctionDAO.getAuctionByKeyword(article, userID, session.getCreationTime()));
             ctx.setVariable("closedauctions", auctionDAO.getWonAuctions(userID));
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_HOME_PAGE).toString());
            throw new RuntimeException(e);
        }
        getTemplateEngine().process(template, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}