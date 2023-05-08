package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.DAOs.ProductDAO;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import com.christian.rossi.progetto_tiw_2023.Utils.InputChecker;


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

import static com.christian.rossi.progetto_tiw_2023.Utils.FileNameChanger.getFileName;


@WebServlet("/doaddproduct")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class DoAddProduct extends ThymeleafHTTPServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {



        final String articleID = request.getParameter("articleID");
        String uploadPath = "C:\\Users\\chris\\OneDrive\\Documents\\GitHub\\tiw-2023-rossi-sharoubim\\Pure HTML\\src\\main\\webapp\\uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        for (Part part : request.getParts()) {
            String fileName = getFileName(part, articleID);
            part.write(uploadPath + File.separator + fileName);
        }

        HttpSession session = request.getSession();
        final String name = request.getParameter("name");
        final String description = request.getParameter("description");
        final int price = Integer.parseInt(request.getParameter("price"));
        final Long userID = (Long) session.getAttribute("userID");


        if (articleID == null || articleID.isEmpty() ||
                name == null || name.isEmpty() ||
                description == null || description.isEmpty()) {
            //TODO: pagina di errore (campi mancanti)
            return;
        }
        if (!InputChecker.checkArticleID(articleID)) {
            //TODO: errore, l'username non rispetta i canoni previsti
            return;
        }
        if (!InputChecker.checkName(name)) {
            //TODO: errore, l'email non rispetta i canoni previsti
            return;
        }
        if (!InputChecker.checkDescription(description)) {
            //TODO: errore, la città non rispetta i canoni previsti
            return;
        }

        ProductDAO productDAO = null;
        try {
            productDAO = new ProductDAO();
            productDAO.addProduct(articleID, name, description, price, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO: pagina di errore (connessione al DB o query)
        }
        response.sendRedirect("/sell");
    }



}
