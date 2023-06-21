package com.christian.rossi.progetto_tiw_2023.Servlets.Controllers;

import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DoGetImage", urlPatterns = {URLs.DO_GET_IMAGE})
public class DoGetImage extends HttpServlet {
    String folderPath = "";

    public void init() {
        folderPath = getServletContext().getInitParameter("outputPath");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.MISSING_FILE).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
            return;
        }
        String filename = URLDecoder.decode(pathInfo.substring(1), StandardCharsets.UTF_8);
        File file = new File(folderPath, filename);
        if (!file.exists() || file.isDirectory()) {
            response.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.MISSING_FILE).addParam("redirect", URLs.GET_OFFERS_PAGE).toString());
            return;
        }
        Files.copy(file.toPath(), response.getOutputStream());
    }
}