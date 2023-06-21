package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;


import com.christian.rossi.progetto_tiw_2023.Constants.Constants;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "doAddProduct", urlPatterns = {Constants.DO_CREATE_AUCTION})
@MultipartConfig
public class DoCreateAuction extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {



    }
}
