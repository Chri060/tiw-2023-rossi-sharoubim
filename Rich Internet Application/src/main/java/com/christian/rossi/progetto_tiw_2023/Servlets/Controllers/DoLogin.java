package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;


import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "Login", urlPatterns = {Constants.LOGIN_ENDPOINT})
@MultipartConfig
public class DoLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UserBean userBean;
        try {
            UserDAO userDAO = new UserDAO();
            userBean = userDAO.authenticate(username, password);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        if (userBean == null) response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        else {
            request.getSession().setAttribute("userName", userBean.getUsername());
            request.getSession().setAttribute("userID", userBean.getUserID());
            response.getWriter().write(userBean.getUsername());
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}