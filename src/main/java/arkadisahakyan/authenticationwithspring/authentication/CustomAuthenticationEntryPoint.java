package arkadisahakyan.authenticationwithspring.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ViewResolver viewResolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof UsernameNotFoundException || authException instanceof BadCredentialsException) {
            try {
                View view = viewResolver.resolveViewName("login", Locale.ENGLISH);
                Map<String, Object> model = new HashMap<>();
                model.put("incorrectUsernameOrPassword", true);
                view.render(model, request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            response.sendRedirect("/login");
    }
}
