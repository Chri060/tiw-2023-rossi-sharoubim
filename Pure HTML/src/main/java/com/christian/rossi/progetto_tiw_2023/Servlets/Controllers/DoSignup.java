package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Beans.UserBean;
import com.christian.rossi.progetto_tiw_2023.DAOs.UserDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;

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



        //TODO: check duplicati
        if (username == null || username.isEmpty() ||
            email == null || email.isEmpty() ||
            city == null || city.isEmpty() ||
            address == null || address.isEmpty() ||
            province == null || province.isEmpty() ||
            password == null || password.isEmpty() ||
            password1 == null || password1.isEmpty()) {
            //TODO: pagina di errore (campi mancanti)
            return;
        }

        if (!InputChecker.checkUsername(username)) {
            //TODO:errore, l'username non rispetta i canoni previsti
            return;
        }
        if (!InputChecker.checkEmail(email)) {
            //TODO:errore, l'email non rispetta i canoni previsti
            return;
        }
        if (!InputChecker.checkCity(city)) {
            //TODO:errore, la città non rispetta i canoni previsti
            return;
        }
        if (!InputChecker.checkAddress(address)) {
            //TODO:errore, l'indirizzo non rispetta i canoni previsti
            return;
        }
        if (!InputChecker.checkProvince(province)) {
            //TODO:errore, la provincia non rispetta i canoni previsti
            return;
        }
        if (!InputChecker.checkPassword(password) || !password.equals(password1)) {
            //TODO:errore, la password non rispetta i canoni previsti oppure non sono uguali
            return;
        }

        UserBean u = null;
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO();
            u = userDAO.addUser(username, email, city, address, province, password);
        } catch (SQLException e) {
            //TODO: pagina di errore (connessione al DB o query)
        }
        request.getSession().setAttribute("user", u.getUsername());
        request.getSession().setAttribute("userID", u.getId());
        response.sendRedirect("/home");
    }
}