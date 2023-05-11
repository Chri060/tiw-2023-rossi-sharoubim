package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.*;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DoLogin", urlPatterns = {URLs.DO_LOGIN})
public class DoLogin extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        if (username == null || username.isEmpty()) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NO_USERNAME).addParam("redirect", URLs.GET_LOGIN_PAGE).toString());
            return;
        }
        if (password == null || password.isEmpty()) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NO_PASSWORD).addParam("redirect", URLs.GET_LOGIN_PAGE).toString());
            return;
        }
        try {
            UserDAO userDAO = new UserDAO();
            UserBean userBean = userDAO.authenticate(username, password);
            if (userBean == null) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NO_USER).addParam("redirect", URLs.GET_LOGIN_PAGE).toString());
            } else {
                request.getSession().setAttribute("user", userBean.getUsername());
                request.getSession().setAttribute("userID", userBean.getUserID());
                response.sendRedirect(URLs.GET_HOME_PAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_LOGIN_PAGE).toString());
        }

    }
}