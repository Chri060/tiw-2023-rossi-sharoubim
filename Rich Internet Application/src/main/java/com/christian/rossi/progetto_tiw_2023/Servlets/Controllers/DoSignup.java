package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;
import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.DAOs.*;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
@WebServlet(name = "Register", urlPatterns = {Constants.REGISTER_ENDPOINT})
@MultipartConfig
public class DoSignup extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String username = request.getParameter("username");
        final String email = request.getParameter("email");
        final String city = request.getParameter("city");
        final String address = request.getParameter("address");
        final String province = request.getParameter("province");
        final String password = request.getParameter("password");

        if (username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                city == null || city.isEmpty() ||
                address == null || address.isEmpty() ||
                province == null || province.isEmpty() ||
                password == null || password.isEmpty() ||
                !InputChecker.checkUsername(username) ||
                !InputChecker.checkEmail(email) ||
                !InputChecker.checkPassword(password) ||
                !InputChecker.checkCity(city) ||
                !InputChecker.checkProvince(province) ||
                !InputChecker.checkAddress(address)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            UserDAO userDAO = new UserDAO();
            if (userDAO.GetUserByUsername(username) == null && userDAO.GetUserByEmail(email) == null) {
                UserBean userBean = userDAO.addUser(username, email, city, address, province, password);
                request.getSession().setAttribute("user", userBean.getUsername());
                request.getSession().setAttribute("userID", userBean.getUserID());
                response.setStatus(HttpServletResponse.SC_OK);
            } else response.setStatus(HttpServletResponse.SC_CONFLICT);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}