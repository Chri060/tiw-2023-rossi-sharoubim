package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Constants;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.http.HttpSession;

@WebServlet(name = "GetSellPage", urlPatterns = {Constants.GET_SELL_PAGE})
@MultipartConfig
public class GetSellPage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {



        HttpSession session = request.getSession();

        try {
            ProductDAO productDAO = new ProductDAO();
            productDAO.getUserProducts((Long) session.getAttribute("userID"));
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }
}