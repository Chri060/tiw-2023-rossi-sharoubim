package com.christian.rossi.progetto_tiw_2023.Servlets.Filters;

import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "UnAuthFilter", urlPatterns = {URLs.DO_LOGIN, URLs.DO_SIGNUP,URLs.GET_LOGIN_PAGE, URLs.GET_SIGNUP_PAGE})
public class UnAuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;
        HttpSession session = hrequest.getSession();
        if (session.getAttribute("user") != null) {
            hresponse.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.UN_AUTH_ERROR).addParam("redirect", URLs.GET_HOME_PAGE).toString());
            return;
        }
        //filter chain pattern
        chain.doFilter(request, response);
    }
}