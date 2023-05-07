package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.OfferDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
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

@WebServlet("/details")
public class GetDetailsPage extends ThymeleafHTTPServlet {
    private String details;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            final String template = "details";
            final ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());


            OfferDAO offerDAO = new OfferDAO();
            AuctionDAO auctionDAO = null;
            UserDAO userDAO = null;
            try {
                auctionDAO = new AuctionDAO();
                offerDAO = new OfferDAO();
                userDAO = new UserDAO();

                List<AuctionBean> auctionBeanList = auctionDAO.getAuction(details);



                ctx.setVariable("selectedauction", auctionBeanList);
                ctx.setVariable("offer", offerDAO.getOffers(details));
                ctx.setVariable("winner", auctionDAO.getWinner(details));
                ctx.setVariable("close", details);
                ctx.setVariable("active", auctionBeanList.get(0).isActive());
                if (auctionDAO.getWinner(details) != null) {
                    ctx.setVariable("user", userDAO.getUser(String.valueOf(auctionDAO.getWinner(details).getUserID())));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            getTemplateEngine().process(template, ctx, response.getWriter());
        } else {
            response.sendRedirect("/login");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        details = request.getParameter("details");
        this.doGet(request, response);
    }
}