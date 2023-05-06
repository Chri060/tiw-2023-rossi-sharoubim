package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.ProductBean;
import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/doaddproduct")
public class DoAddProduct extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        final String articleID = request.getParameter("articleID");
        final String name = request.getParameter("name");
        final String description = request.getParameter("description");
        final int price = Integer.parseInt(request.getParameter("price"));
        final Long userID = (Long) session.getAttribute("userID");


        if (articleID == null || articleID.isEmpty() ||
                name == null || name.isEmpty() ||
                description == null || description.isEmpty()) {
            //TODO: pagina di errore (campi mancanti)
            return;
        }
        if (!InputChecker.checkArticleID(articleID)) {
            //TODO: errore, l'username non rispetta i canoni previsti
            return;
        }
        if (!InputChecker.checkName(name)) {
            //TODO: errore, l'email non rispetta i canoni previsti
            return;
        }
        if (!InputChecker.checkDescription(description)) {
            //TODO: errore, la città non rispetta i canoni previsti
            return;
        }

        ProductDAO productDAO = null;
        try {
            productDAO = new ProductDAO();
            productDAO.addProduct(articleID, name, description, price, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO: pagina di errore (connessione al DB o query)
        }
        response.sendRedirect("/sell");
    }
}
