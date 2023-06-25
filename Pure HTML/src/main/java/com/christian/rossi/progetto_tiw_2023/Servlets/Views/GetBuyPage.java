package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Beans.AuctionBean;
import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.AuctionDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
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
import java.util.List;

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
            ProductDAO productDAO = new ProductDAO();
            List<AuctionBean> auctions = null;
            if (article != null && !article.equals("")) auctions = auctionDAO.getAuctionByKeyword(article, userID, session.getCreationTime());
            if (auctions != null) {
                for (AuctionBean auctionBean : auctions) {
                    List<ProductBean> productBeanList = productDAO.getProductFromAuction(auctionBean.getAuctionID());
                    auctionBean.setProductList(productBeanList);
                    String products = "";
                    for (ProductBean productBean : productBeanList) {
                        products += productBean.getName() + "</br>";
                    }
                    auctionBean.setProductNames(products);
                }
            }
            List<AuctionBean> closedauctions = auctionDAO.getWonAuctions(userID);
            if (closedauctions != null) {
                for (AuctionBean auctionBean : closedauctions) {
                    List<ProductBean> productBeanList = productDAO.getProductFromAuction(auctionBean.getAuctionID());
                    auctionBean.setProductList(productBeanList);
                    String products = "";
                    for (ProductBean productBean : productBeanList) {
                        products += productBean.getName() + "</br>";
                    }
                    auctionBean.setProductNames(products);
                }
            }
            ctx.setVariable("auctions", auctions);
            ctx.setVariable("closedauctions", closedauctions);
        }
        catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_HOME_PAGE).toString());
            throw new RuntimeException(e);
        }
        catch (NullPointerException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.GENERIC_ERROR).addParam("redirect", URLs.GET_HOME_PAGE).toString());
        }
        getTemplateEngine().process(template, ctx, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}