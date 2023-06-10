package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
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

@WebServlet(name = "GetSellPage", urlPatterns = {URLs.GET_SELL_PAGE})
public class GetSellPage extends ThymeleafHTTPServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        final String template = "sell";
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        try {
            ProductDAO productDAO = new ProductDAO();
            AuctionDAO auctionDAO = new AuctionDAO();
            ctx.setVariable("products", productDAO.getUserProducts((Long) session.getAttribute("userID")));
            ctx.setVariable("closedauctions", auctionDAO.getAuctions((Long) session.getAttribute("userID"), 0, session.getCreationTime()));
            ctx.setVariable("activeauctions", auctionDAO.getAuctions((Long) session.getAttribute("userID"), 1, session.getCreationTime()));
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_HOME_PAGE).toString());
            throw new RuntimeException(e);
        }
        getTemplateEngine().process(template, ctx, response.getWriter());
    }
}