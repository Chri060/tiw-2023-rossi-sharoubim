package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "DefaultPage", urlPatterns = {"","/"})
public class LoadDefaultPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("USER") != null) {
            response.sendRedirect(new PathBuilder(Constants.HOME_PAGE).toString());
        }
        else {
            response.sendRedirect(new PathBuilder(Constants.AUTHENTICATION_PAGE).toString());
        }
    }
}
