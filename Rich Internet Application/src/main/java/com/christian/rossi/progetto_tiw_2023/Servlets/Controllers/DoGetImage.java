package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Constants;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DoGetImage", urlPatterns = {Constants.DO_GET_IMAGE})
@MultipartConfig
public class DoGetImage extends HttpServlet {

    String folderPath = "";

    public void init() {
        folderPath = getServletContext().getInitParameter("outputPath");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) return;
        String filename = URLDecoder.decode(pathInfo.substring(1), StandardCharsets.UTF_8);
        File file = new File(folderPath, filename);
        if (!file.exists() || file.isDirectory()) return;
        Files.copy(file.toPath(), response.getOutputStream());
    }
}