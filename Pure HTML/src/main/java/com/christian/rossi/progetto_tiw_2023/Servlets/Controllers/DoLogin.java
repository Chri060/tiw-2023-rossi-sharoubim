package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;

import com.christian.rossi.progetto_tiw_2023.Servlets.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/dologin")
public class DoLogin extends ThymeleafHTTPServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            //TODO: pagina di errore (username o password mancanti)
            return;
        }
        UserBean u = null;
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            u = userDAO.authenticate(username, password);
        } catch (SQLException e) {
            //TODO: pagina di errore (connessione al DB o query)
        }
        if (u == null) {
            //TODO: pagina di errore (utente non trovato)
            response.sendRedirect("/login");
        } else {
            request.getSession().setAttribute("user", u.getUsername());
            request.getSession().setAttribute("userID", u.getId());
            response.sendRedirect("/home");
        }
    }
}