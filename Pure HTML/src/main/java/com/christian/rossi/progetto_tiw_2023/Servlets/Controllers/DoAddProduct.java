package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

@WebServlet(name = "DoAddProduct", urlPatterns = {URLs.DO_ADD_PRODUCT})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class DoAddProduct extends ThymeleafHTTPServlet {
    String folderPath = "";

    public void init() throws ServletException {
        folderPath = getServletContext().getInitParameter("outputPath");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //TODO: controllo input
        HttpSession session = request.getSession();
        final String name = request.getParameter("name");
        final String description = request.getParameter("description");
        final int price = Integer.parseInt(request.getParameter("price"));
        final Long userID = (Long) session.getAttribute("userID");
        final String articleID = request.getParameter("articleID");
        //start of file uploading
        Part filePart = request.getPart("file");
        if (filePart == null || filePart.getSize() <= 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file in request!");
            return;
        }
        String contentType = filePart.getContentType();
        if (!contentType.startsWith("image")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File format not permitted");
            return;
        }
        String fileName = articleID + ".jpeg";
        String outputPath = folderPath + fileName;
        File file = new File(outputPath);
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, file.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while saving file");
        }
        //end of file uploading
        try {
            ProductDAO productDAO = new ProductDAO();
            productDAO.addProduct(articleID, name, description, price, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while trying to retrieve data from the database");
        }
        response.sendRedirect("/sell");
    }
}