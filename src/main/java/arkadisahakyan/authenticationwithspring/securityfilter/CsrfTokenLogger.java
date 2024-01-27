package arkadisahakyan.authenticationwithspring.securityfilter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

public class CsrfTokenLogger implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Object o = servletRequest.getAttribute("_csrf");
        CsrfToken token = (CsrfToken) o;
        System.out.println("CSRF token " + token.getToken());
        Cookie sessionId = WebUtils.getCookie((HttpServletRequest) servletRequest, "JSESSIONID");
        System.out.println("SESSION " + (sessionId == null ? "null" : sessionId.getValue()));
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
