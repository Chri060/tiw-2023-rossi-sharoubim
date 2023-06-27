package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.*;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "GetBuyPage", urlPatterns = {URLs.GET_BUY_PAGE})
public class GetBuyPage extends ThymeleafHTTPServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        final String template = "buy";
        final Long userID;
        String article = request.getParameter("search");
        userID = (Long) session.getAttribute("userID");
        try {
            AuctionDAO auctionDAO = new AuctionDAO();
            List<AuctionBean> auctions = null;
            if (article != null && !article.equals("")) auctions = auctionDAO.getAuctionByKeyword(article, userID, session.getCreationTime());
            StringFormatter.getProductsWithBreakLine(auctions);
            List<AuctionBean> closedAuctions = auctionDAO.getWonAuctions(userID);
            StringFormatter.getProductsWithBreakLine(closedAuctions);
            ctx.setVariable("auctions", auctions);
            ctx.setVariable("closedauctions", closedAuctions);
        }
        catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_HOME_PAGE).toString());
            throw new RuntimeException(e);
        }
        catch (NullPointerException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.GENERIC_ERROR).addParam("redirect", URLs.GET_HOME_PAGE).toString());
            return;
        }
        getTemplateEngine().process(template, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}