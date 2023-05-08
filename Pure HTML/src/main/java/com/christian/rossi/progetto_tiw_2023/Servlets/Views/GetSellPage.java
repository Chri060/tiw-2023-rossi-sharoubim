package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/sell")
public class GetSellPage extends ThymeleafHTTPServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            final String template = "sell";
            final ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());


            ProductDAO productDAO = null;
            AuctionDAO auctionDAO = null;
            try {
                productDAO = new ProductDAO();
                auctionDAO = new AuctionDAO();
                ctx.setVariable("products", productDAO.getUserProducts((Long) session.getAttribute("userID")));
                ctx.setVariable("closedauctions", auctionDAO.getAuctions((Long) session.getAttribute("userID"), 0, session.getCreationTime()));
                ctx.setVariable("activeauctions", auctionDAO.getAuctions((Long) session.getAttribute("userID"), 1, session.getCreationTime()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            getTemplateEngine().process(template, ctx, response.getWriter());
        } else {
            response.sendRedirect("/login");
        }
    }
}