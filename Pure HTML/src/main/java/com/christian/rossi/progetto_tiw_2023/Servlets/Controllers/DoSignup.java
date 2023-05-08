package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/dosignup")
public class DoSignup extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String username = request.getParameter("username");
        final String email = request.getParameter("email");
        final String city = request.getParameter("city");
        final String address = request.getParameter("address");
        final String province = request.getParameter("province");
        final String password = request.getParameter("password");
        final String password1 = request.getParameter("password1");
        try {
            UserDAO userDAO = new UserDAO();
            UserBean userBean = userDAO.addUser(username, email, city, address, province, password);
            request.getSession().setAttribute("user", userBean.getUsername());
            request.getSession().setAttribute("userID", userBean.getUserID());
        } catch (SQLException e) {
            //TODO: pagina di errore (connessione al DB o query)
        }
        response.sendRedirect("/home");
    }
}