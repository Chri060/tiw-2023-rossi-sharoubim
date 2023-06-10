package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DoSignup", urlPatterns = {URLs.DO_SIGNUP})
public class DoSignup extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String username;
        final String email;
        final String city;
        final String address;
        final String province;
        final String password;
        final String repeatedPassword;

        //checking problems with variable username
        username = request.getParameter("username");
        if (username == null || username.isEmpty() || !InputChecker.checkUsername(username)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.USERNAME_ERROR).addParam("redirect", URLs.GET_SIGNUP_PAGE).toString());
            return;
        }

        //checking problems with variable email
        email = request.getParameter("email");
        if (email == null || email.isEmpty() || !InputChecker.checkEmail(email)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.EMAIL_ERROR).addParam("redirect", URLs.GET_SIGNUP_PAGE).toString());
            return;
        }

        //checking problems with variable city
        city = request.getParameter("city");
        if (city == null || city.isEmpty() || !InputChecker.checkCity(city)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.CITY_ERROR).addParam("redirect", URLs.GET_SIGNUP_PAGE).toString());
            return;
        }

        //checking problems with variable address
        address = request.getParameter("address");
        if (address == null || address.isEmpty() || !InputChecker.checkAddress(address)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.ADDRESS_ERROR).addParam("redirect", URLs.GET_SIGNUP_PAGE).toString());
            return;
        }

        //checking problems with variable province
        province = request.getParameter("province");
        if (province == null || province.isEmpty() || !InputChecker.checkProvince(province)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.PROVINCE_ERROR).addParam("redirect", URLs.GET_SIGNUP_PAGE).toString());
            return;
        }

        //checking problems with variable password and password1
        password = request.getParameter("password");
        repeatedPassword = request.getParameter("password1");
        if (password == null || password.isEmpty() || repeatedPassword == null || repeatedPassword.isEmpty() || !password.equals(repeatedPassword) || !InputChecker.checkPassword(password)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.PASSWORD_ERROR).addParam("redirect", URLs.GET_SIGNUP_PAGE).toString());
            return;
        }

        //check the uniqueness of some parameters
        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.GetUserByUsername(username) != null) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.USERNAME_NOT_UNIQUE).addParam("redirect", URLs.GET_SIGNUP_PAGE).toString());
                return;
            }
            if (userDAO.GetUserByEmail(email) != null) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.EMAIL_NOT_UNIQUE).addParam("redirect", URLs.GET_SIGNUP_PAGE).toString());
                return;
            }
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SIGNUP_PAGE).toString());
            return;
        }

        //checking complete, adding the user to the database and login
        try {
            UserDAO userDAO = new UserDAO();
            UserBean userBean = userDAO.addUser(username, email, city, address, province, password);
            request.getSession().setAttribute("user", userBean.getUsername());
            request.getSession().setAttribute("userID", userBean.getUserID());
        } catch (SQLException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SIGNUP_PAGE).toString());
        }
        response.sendRedirect(URLs.GET_HOME_PAGE);
    }
}