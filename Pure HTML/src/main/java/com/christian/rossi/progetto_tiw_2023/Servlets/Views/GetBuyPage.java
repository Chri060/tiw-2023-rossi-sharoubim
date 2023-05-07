package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/buy")
public class GetBuyPage extends ThymeleafHTTPServlet {
    private String article;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            final ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
            final String template = "buy";


            Long userID = (Long) session.getAttribute("userID");
            AuctionDAO auctionDAO = null;
            try {
                auctionDAO = new AuctionDAO();
                if (article != null) {
                    ctx.setVariable("auctions", auctionDAO.getAuctionByKeyword(article, userID));
                }
                ctx.setVariable("closedauctions", auctionDAO.getWonAuctions((Long) session.getAttribute("userID")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            getTemplateEngine().process(template, ctx, response.getWriter());
        } else {
            response.sendRedirect("/login");
        }
        article = null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        article = request.getParameter("search");
        doGet(request, response);
    }
}