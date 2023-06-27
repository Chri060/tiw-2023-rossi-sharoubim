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
import java.sql.SQLException;

@WebServlet(name = "DoAddProduct", urlPatterns = {URLs.DO_ADD_PRODUCT})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class DoAddProduct extends ThymeleafHTTPServlet {

    String folderPath = "";

    public void init() {
        folderPath = getServletContext().getInitParameter("outputPath");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        final String name;
        final String description;
        final int price;
        final Long userID;
        int statusCode = 0;
        userID = (Long) session.getAttribute("userID");
        name = request.getParameter("name");
        if (name == null || name.isEmpty() || !InputChecker.checkName(name)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NAME_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        description = request.getParameter("description");
        if (description == null || description.isEmpty() || !InputChecker.checkDescription(description)) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DESCRIPTION_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
        try {
            price = Integer.parseInt(request.getParameter("price"));
            if (!InputChecker.checkPrice(price)) {
                response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.PRICE_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
                return;
            }
        }
        catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.NUMBER_FORMAT_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            return;
        }
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
            catch (SQLException e) { /*do nothing*/}
        }
        switch (statusCode) {
            case 0 -> response.sendRedirect(URLs.GET_SELL_PAGE);
            case -1 -> response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.SAVE_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
            case -2 -> response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.DB_ERROR).addParam("redirect", URLs.GET_SELL_PAGE).toString());
        }
    }
}