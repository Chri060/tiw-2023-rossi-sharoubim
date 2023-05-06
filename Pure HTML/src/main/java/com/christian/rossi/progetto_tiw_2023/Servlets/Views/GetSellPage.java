package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
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
            try {
                productDAO = new ProductDAO();
                ctx.setVariable("products", productDAO.getUserProducts((Long) session.getAttribute("userID")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            getTemplateEngine().process(template, ctx, response.getWriter());
        } else {
            response.sendRedirect("/login");
        }
    }
}