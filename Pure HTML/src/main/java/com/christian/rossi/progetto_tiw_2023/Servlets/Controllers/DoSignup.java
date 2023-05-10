package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DoSignup", urlPatterns = {URLs.DO_SIGNUP})
public class DoSignup extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //TODO: controllo input
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
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while trying to retrieve data from the database");
        }
        response.sendRedirect("/home");
    }
}