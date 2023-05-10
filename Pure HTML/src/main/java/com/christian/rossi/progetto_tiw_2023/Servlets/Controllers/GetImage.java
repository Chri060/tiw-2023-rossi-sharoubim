package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getImage/*")
public class GetImage extends HttpServlet {
    String folderPath = "";

    public void init() throws ServletException {
        folderPath = getServletContext().getInitParameter("outputPath");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing file name!");
            return;
        }

        String filename = URLDecoder.decode(pathInfo.substring(1), StandardCharsets.UTF_8);

        File file = new File(folderPath, filename);
        System.out.println(filename);

        if (!file.exists() || file.isDirectory()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not present");
            return;
        }

        Files.copy(file.toPath(), response.getOutputStream());
    }
}


