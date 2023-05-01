package com.christian.rossi.progetto_tiw_2023.Servlets.Views;

import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.Servlets.ThymeleafHTTPServlet;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Home", urlPatterns = {URLs.HOMEPAGE})
public class GetHomePage extends ThymeleafHTTPServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String template = "home";
        final ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        getTemplateEngine().process(template, ctx, response.getWriter());
    }
}