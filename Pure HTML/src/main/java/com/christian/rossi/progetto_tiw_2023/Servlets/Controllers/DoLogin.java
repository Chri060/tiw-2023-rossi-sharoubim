package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DoLogin", urlPatterns = {URLs.DO_LOGIN})
public class DoLogin extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //TODO: controllo input
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        try {
            UserDAO userDAO = new UserDAO();
            UserBean userBean = userDAO.authenticate(username, password);
            if (userBean == null) {
                //TODO: pagina di errore (utente non trovato)
                response.sendRedirect("/login");
            } else {
                request.getSession().setAttribute("user", userBean.getUsername());
                request.getSession().setAttribute("userID", userBean.getUserID());
                response.sendRedirect("/home");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while trying to retrieve data from the database");
        }
    }
}