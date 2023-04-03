package com.christian.rossi.progetto_tiw_2023;

import java.io.*;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>" +
                    "<p> Hello world! </p>" +
                    "</body></html>");
    }

    public void destroy() {
    }
}