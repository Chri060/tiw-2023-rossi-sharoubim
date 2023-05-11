package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
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
        HttpSession session = request.getSession();
        final String name = request.getParameter("name");
        final String description = request.getParameter("description");
        final int price = Integer.parseInt(request.getParameter("price"));
        final Long userID = (Long) session.getAttribute("userID");
        final String productID = request.getParameter("articleID");
        if (name == null || name.isEmpty() || !InputChecker.checkName(name)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NAME_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        if (description == null || description.isEmpty() || !InputChecker.checkDescription(description)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DESCRIPTION_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        if (!InputChecker.checkPrice(price)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.PRICE_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        if (!InputChecker.checkProductID(productID)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.PRODUCT_ID_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        try {
            ProductDAO productDAO = new ProductDAO();
            if (productDAO.GetProductByID(Long.valueOf(productID)) != null) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.PRODUCT_ID_NOT_UNIQUE).addParam("redirect", URLs.GET_SELL_PAGE).toString());
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }

        //start of file uploading
        Part filePart = request.getPart("file");
        if (filePart == null || filePart.getSize() <= 0) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.MISSING_FILE).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        String contentType = filePart.getContentType();
        if (!contentType.startsWith("image")) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.FORMAT_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        String fileName = productID + ".jpeg";
        String outputPath = folderPath + fileName;
        File file = new File(outputPath);
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, file.toPath());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.SAVE_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        //end of file uploading
        try {
            ProductDAO productDAO = new ProductDAO();
            productDAO.addProduct(productID, name, description, price, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        response.sendRedirect(URLs.GET_SELL_PAGE);
    }
}