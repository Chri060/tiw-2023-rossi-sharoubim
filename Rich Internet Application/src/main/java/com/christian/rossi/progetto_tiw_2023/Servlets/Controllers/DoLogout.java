package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Constants;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Logout", urlPatterns = {Constants.LOGOUT_ENDPOINT})
@MultipartConfig
public class DoLogout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.getSession().invalidate();
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }
}