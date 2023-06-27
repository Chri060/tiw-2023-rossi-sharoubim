package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;

@WebServlet(name = "doAddProduct", urlPatterns = {Constants.DO_ADD_PRODUCT})
@MultipartConfig
public class DoAddProduct extends HttpServlet {

    String folderPath = "";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        final String name;
        final String description;
        final int price;
        final Long userID;
        int statusCode = 0;
        folderPath = getServletContext().getInitParameter("outputPath");
        name = request.getParameter("name");
        if (name == null || name.isEmpty() || !InputChecker.checkName(name)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        description = request.getParameter("description");
        if (description == null || description.isEmpty() || !InputChecker.checkDescription(description)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            price = Integer.parseInt(request.getParameter("price"));
            if (!InputChecker.checkPrice(price)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        userID = (Long) session.getAttribute("userID");
        Part filePart = request.getPart("file");
        if (filePart == null || filePart.getSize() <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String contentType = filePart.getContentType();
        if (!contentType.startsWith("image")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        ProductDAO productDAO = new ProductDAO();
        try {
            productDAO.setAutoCommit(false);
            long productID = productDAO.addProduct(name, description, price, userID);
            String fileName = productID + ".jpeg";
            String outputPath = folderPath + fileName;
            File file = new File(outputPath);
            InputStream fileContent = filePart.getInputStream();
            Files.copy(fileContent, file.toPath());
        } catch (SQLException e) {
            try { productDAO.rollback(); }
            catch (SQLException exception) { /*do nothing*/ }
            statusCode = -2;
        }
        catch (Exception e) { statusCode = - 1; }
        finally {
            try { productDAO.setAutoCommit(true); }
            catch (SQLException e) { /*do nothing*/ }
        }
        switch (statusCode) {
            case 0 -> response.setStatus(HttpServletResponse.SC_OK);
            case -1 -> response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            case -2 -> response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}