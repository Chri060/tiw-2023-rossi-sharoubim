package com.christian.rossi.progetto_tiw_2023.Servlets.Filters;

import com.christian.rossi.progetto_tiw_2023.Constants.URLs;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {URLs.DO_ADD_PRODUCT, URLs.DO_CLOSE, URLs.DO_CREATE_AUCTION, URLs.DO_GET_IMAGE, URLs.DO_OFFER, URLs.GET_BUY_PAGE, URLs.GET_DETAILS_PAGE, URLs.GET_HOME_PAGE, URLs.GET_OFFERS_PAGE, URLs.GET_SELL_PAGE})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;
        HttpSession session = hrequest.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            //TODO:errore
            hresponse.sendRedirect("/login");
            return;
        }
        //filter chain pattern
        chain.doFilter(request, response);
    }
}