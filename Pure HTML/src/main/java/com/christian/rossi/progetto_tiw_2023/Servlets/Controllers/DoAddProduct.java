package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

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
import java.sql.SQLException;

@WebServlet("/doaddproduct")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class DoAddProduct extends ThymeleafHTTPServlet {
    final String uploadPath = "C:\\Users\\chris\\OneDrive\\Documents\\GitHub\\tiw-2023-rossi-sharoubim\\Pure HTML\\src\\main\\webapp\\images";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        final String name = request.getParameter("name");
        final String description = request.getParameter("description");
        final int price = Integer.parseInt(request.getParameter("price"));
        final Long userID = (Long) session.getAttribute("userID");
        final String articleID = request.getParameter("articleID");
        //start of file uploading
        File uploadDir = new File(uploadPath);
        for (Part part : request.getParts()) {
            String fileName = FileNameChanger.getFileName(part, articleID);
            part.write(uploadPath + File.separator + fileName);
        }
        //end of file uploading

        //TODO:check for correct input

        try {
            ProductDAO productDAO = new ProductDAO();
            productDAO.addProduct(articleID, name, description, price, userID);
        } catch (SQLException e) {
            //TODO: pagina di errore per problemi con la query
        }
        response.sendRedirect("/sell");
    }
}