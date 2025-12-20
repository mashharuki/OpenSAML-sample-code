package no.steras.opensamlSamples.opensaml4WebprofileDemo.app;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet acts as the resource that the access filter is protecting
 */
public class ApplicationServlet extends HttpServlet {
    /**
     * Handles the HTTP <code>GET</code> method.
     * 保護ページ SAML認証を突破しないと表示されないリソース
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.getWriter().append("<h1>You are now at the requested resource</h1>");
        resp.getWriter().append("This is the protected resource. You are authenticated");
    }
}
